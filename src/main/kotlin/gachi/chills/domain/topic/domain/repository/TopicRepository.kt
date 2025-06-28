package gachi.chills.domain.topic.domain.repository

import gachi.chills.domain.topic.domain.model.Topic
import gachi.chills.global.base.BaseJpaRepository

interface TopicRepository : BaseJpaRepository<Topic, Long>
