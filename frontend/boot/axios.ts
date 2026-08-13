import { defineBoot } from '#q-app/wrappers'
import axios, { type AxiosInstance, AxiosError, InternalAxiosRequestConfig } from 'axios'
import { Credentials } from 'src/features/auth/composables/credentials'
import { AuthApi } from 'src/features/auth/api'
import qs from 'qs'
import { Notify } from 'quasar'

declare module 'vue' {
  interface ComponentCustomProperties {
    $axios: AxiosInstance
    $api: AxiosInstance
  }
}
declare module 'axios' {
  interface AxiosRequestConfig {
    __isRetryRequest?: boolean
    __isRefreshTokenRequest?: boolean
  }
}

const AUTH_HEADER = 'X-Authorization'
const apiUrl = import.meta.env.VITE_API_URL ?? ''

const api = axios.create({
  baseURL: apiUrl,
  paramsSerializer: (params) => {
    return qs.stringify(params, { arrayFormat: 'comma' })
  },
})

let isRefreshing = false
let failedRequests: {
  resolve: (value: unknown) => void
  reject: (reason?: unknown) => void
  config: InternalAxiosRequestConfig
}[] = []

const retryFailedRequests = (newToken: string) => {
  failedRequests.forEach((request) => {
    request.config.headers![AUTH_HEADER] = `Bearer ${newToken}`
    api(request.config).then(request.resolve).catch(request.reject)
  })
  failedRequests = []
}

const rejectFailedRequests = (error: unknown) => {
  failedRequests.forEach((request) => request.reject(error))
  failedRequests = []
}

api.interceptors.request.use((config) => {
  const accessToken = Credentials.getAccessToken()

  if (accessToken) {
    config.headers[AUTH_HEADER] = `Bearer ${accessToken}`
  }

  return config
})

export default defineBoot(({ app, router }) => {
  app.config.globalProperties.$axios = axios
  app.config.globalProperties.$api = api
  api.interceptors.response.use(
    (response) => response,
    async (error: AxiosError) => {
      const originalRequest = { ...error.config } as InternalAxiosRequestConfig
      if (error.response?.status === 401 && originalRequest) {
        if (originalRequest?.__isRefreshTokenRequest) {
          Credentials.removeTokens()
          rejectFailedRequests(error)
          isRefreshing = false
          await router.push('/login')
          return Promise.reject(error)
        }
        if (isRefreshing && originalRequest && originalRequest.__isRetryRequest) {
          Credentials.removeTokens()
          isRefreshing = false
          rejectFailedRequests(error)
          await router.push('/login')
          return Promise.reject(error)
        }
        originalRequest.__isRetryRequest = true
        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            failedRequests.push({ resolve, reject, config: originalRequest })
          })
        }
        isRefreshing = true

        try {
          const refreshToken = Credentials.getRefreshToken()
          Credentials.removeTokens()
          delete originalRequest.headers[AUTH_HEADER]
          const tokens = await AuthApi.refreshToken(refreshToken, {
            __isRefreshTokenRequest: true,
          })
          Credentials.setTokens(tokens.data)
          const resultOriginalRequest = await api(originalRequest)
          retryFailedRequests(tokens.data.accessToken)
          return resultOriginalRequest
        } catch (refreshError) {
          Credentials.removeTokens()
          rejectFailedRequests(refreshError)
          await router.push('/login')
        } finally {
          isRefreshing = false
        }
      } else {
        Notify.create({
          type: 'negative',
          message: error.message,
        })
        throw error
      }
    },
  )
})

export { api }
