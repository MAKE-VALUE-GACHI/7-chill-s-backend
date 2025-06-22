package gachi.chills.domain.auth.domain.repository

import gachi.chills.domain.auth.domain.model.UserContext

object UserContextHolder {
    private val holder = ThreadLocal<UserContext>()

    fun get(): UserContext? = holder.get()

    fun store(userContext: UserContext) = holder.set(userContext)

    fun clear() = holder.remove()
}
