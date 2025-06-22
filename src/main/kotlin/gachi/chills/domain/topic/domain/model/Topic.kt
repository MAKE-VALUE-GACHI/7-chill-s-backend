package gachi.chills.domain.topic.domain.model

import gachi.chills.global.base.RecordTrackingBaseJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "topics")
class Topic(
    title: String,
) : RecordTrackingBaseJpaEntity() {
    @Column(name = "title", nullable = false)
    var title: String = title
        private set
}
