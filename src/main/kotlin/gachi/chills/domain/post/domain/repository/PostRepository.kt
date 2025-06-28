package gachi.chills.domain.post.domain.repository

import gachi.chills.domain.post.domain.model.Post
import gachi.chills.global.base.BaseJpaRepository

interface PostRepository : BaseJpaRepository<Post, Long>
