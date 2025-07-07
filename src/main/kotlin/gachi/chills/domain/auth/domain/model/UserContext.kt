package gachi.chills.domain.auth.domain.model

import gachi.chills.domain.user.domain.model.User

data class UserContext(
    val id: String,
    val name: String,
    val email: String,
)

fun User.toUserContext() = UserContext(
    id = id,
    name = name,
    email = email,
)
