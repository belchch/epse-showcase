package dev.epse.app.auth

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.time.Instant

@NoRepositoryBean
interface IRefreshTokenRepository<T : IRefreshToken> : JpaRepository<T, Long> {
    fun findByToken(token: String): T?
}

interface IRefreshToken {
    val token: String
    val username: String
    val expirationDate: Instant
}

interface RefreshTokenRepository : IRefreshTokenRepository<RefreshToken>
