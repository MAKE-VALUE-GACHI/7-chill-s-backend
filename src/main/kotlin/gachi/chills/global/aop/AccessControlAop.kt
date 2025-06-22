package gachi.chills.global.aop

import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
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
    fun checkAccessControl() {
        TODO("Not yet implemented")
    }
}
