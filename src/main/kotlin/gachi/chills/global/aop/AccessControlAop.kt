package gachi.chills.global.aop

import gachi.chills.domain.auth.domain.model.UserContext
import gachi.chills.domain.auth.domain.repository.UserContextHolder
import gachi.chills.domain.auth.exception.AuthExceptionCode
import gachi.chills.global.exception.BusinessException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AccessControl(vararg val allowed: Allowed)

enum class Allowed {
    PUBLIC,
    AUTHENTICATED,
}

@Aspect
@Component
class AccessControlAop {
    @Before("@annotation(gachi.chills.global.aop.AccessControl)")
    fun checkAccessControl(joinPoint: JoinPoint) {
        val accessControl = getAccessControlAnnotation(joinPoint)
        val authModel = UserContextHolder.get()
        if (cannotAcceptRequest(accessControl, authModel)) {
            throw BusinessException(AuthExceptionCode.PERMISSION_DENIED)
        }
    }

    private fun getAccessControlAnnotation(joinPoint: JoinPoint): AccessControl {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        return method.getAnnotation(AccessControl::class.java)
    }

    private fun cannotAcceptRequest(
        accessControl: AccessControl,
        userContext: UserContext?,
    ): Boolean {
        return accessControl.allowed.none {
            when (it) {
                Allowed.PUBLIC -> true
                Allowed.AUTHENTICATED -> userContext != null
            }
        }
    }
}
