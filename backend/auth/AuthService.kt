package dev.epse.app.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Service
import dev.epse.app.config.security.JwtService
import dev.epse.app.user.Role
import dev.epse.app.user.UserCreateRequest
import dev.epse.app.user.UserService

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userService: UserService,
    refreshTokenRepository: RefreshTokenRepository
) {
    val baseAuthService = BaseAuthService(
        authenticationManager = authenticationManager,
        jwtService = jwtService,
        userService = userService,
        refreshTokenRepository = refreshTokenRepository,
        tokenFactory = { token, username, expirationDate ->
            RefreshToken(
                token = token,
                username = username,
                expirationDate = expirationDate
            )
        }
    )

    fun login(username: String, password: String): Pair<String, String> {
        return baseAuthService.login(username, password)
    }

    fun refreshToken(token: String): Pair<String, String> {
        return baseAuthService.refreshToken(token)
    }

    fun register(request: UserCreateRequest): Pair<String, String> {
        val toCreate = if (userService.hasAnyUsers()) {
            request.copy(role = Role.USER)
        } else {
            request
        }
        return baseAuthService.register(toCreate)
    }

    fun logout(refreshToken: String) {
        baseAuthService.logout(refreshToken)
    }
}