package gachi.chills.domain.user.domain.repository

import gachi.chills.domain.user.domain.model.User
import gachi.chills.global.base.BaseJpaRepository

interface UserRepository : BaseJpaRepository<User, String>
