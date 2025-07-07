package gachi.chills.domain.user.domain.repository

import gachi.chills.domain.user.domain.model.User
import gachi.chills.domain.user.exception.UserExceptionCode
import gachi.chills.global.base.BaseJpaRepository
import gachi.chills.global.exception.BusinessException
import org.springframework.data.repository.findByIdOrNull

interface UserRepository : BaseJpaRepository<User, String> {
    fun findByEmail(email: String): User?
}

fun UserRepository.findByIdOrThrow(id: String): User {
    return this.findByIdOrNull(id)
        ?: throw BusinessException(
            code = UserExceptionCode.USER_NOT_FOUND,
            detail = "id=$id",
        )
}
