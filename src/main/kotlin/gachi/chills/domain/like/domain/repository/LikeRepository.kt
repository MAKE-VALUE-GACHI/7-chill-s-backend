package gachi.chills.domain.like.domain.repository

import gachi.chills.domain.like.domain.model.Like
import gachi.chills.global.base.BaseJpaRepository

interface LikeRepository : BaseJpaRepository<Like, Long>
