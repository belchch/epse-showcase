package dev.epse.app.config.security

import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class AppPermissionEvaluator : PermissionEvaluator {
    override fun hasPermission(
        authentication: Authentication?,
        targetDomainObject: Any?,
        permission: Any?
    ): Boolean = hasAuthority(authentication, permission)

    override fun hasPermission(
        authentication: Authentication?,
        targetId: Serializable?,
        targetType: String?,
        permission: Any?
    ): Boolean = hasAuthority(authentication, permission)

    private fun hasAuthority(authentication: Authentication?, permission: Any?): Boolean {
        val required = permission?.toString() ?: return false
        return authentication?.authorities?.any { it.authority == required } == true
    }
}
