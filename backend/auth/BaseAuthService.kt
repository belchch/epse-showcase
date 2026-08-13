package dev.epse.app.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import dev.epse.app.config.security.JwtService
import dev.epse.app.user.UserCreateRequest
import dev.epse.app.user.UserService
import java.time.Instant

class BaseAuthService<T : IRefreshToken>(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userService: UserService,
    private val refreshTokenRepository: IRefreshTokenRepository<T>,
    private val tokenFactory: (token: String, username: String, expirationDate: Instant) -> T
) {
    fun login(username: String, password: String): Pair<String, String> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                username,
                password
            )
        )

        val (accessToken, refreshToken) = generateTokens(username)
        return (accessToken to refreshToken)
    }

    fun refreshToken(token: String): Pair<String, String> {
        val refreshToken = obtainToken(token)
        return generateTokens(refreshToken)
    }

    fun obtainToken(token: String): T {
        if (!jwtService.validateToken(token)) {
            throw BadCredentialsException("Invalid refresh token")
        }

        val refreshToken = refreshTokenRepository.findByToken(token)
            ?: throw BadCredentialsException("Refresh token not found")

        if (jwtService.isTokenExpired(token)) {
            refreshTokenRepository.delete(refreshToken)
            throw BadCredentialsException("Refresh token expired")
        }

        return refreshToken
    }

    private fun generateTokens(refreshToken: T): Pair<String, String> {
        val (newAccessToken, newRefreshToken) = generateTokens(refreshToken.username)
        refreshTokenRepository.delete(refreshToken)
        return (newAccessToken to newRefreshToken)
    }


    fun register(request: UserCreateRequest): Pair<String, String> {
        with(request) {
            userService.createUser(request)
            return generateTokens(username)
        }
    }

    fun generateTokens(username: String): Pair<String, String> {
        val accessToken = jwtService.generateAccessToken(username)
        val refreshToken = jwtService.generateRefreshToken(username)

        refreshTokenRepository.save(tokenFactory(
            refreshToken,
            username,
            jwtService.extractExpiration(refreshToken).toInstant()
        ))

        return (accessToken to refreshToken)
    }

    fun logout(refreshToken: String) {
        refreshTokenRepository.findByToken(refreshToken)?.let {
            refreshTokenRepository.delete(it)
        }
    }
}