package gachi.chills.global.base

import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class RecordTrackingBaseJpaEntity : Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    override fun getId(): Long? = id

    @Transient
    private var _isNew: Boolean = true

    @PostPersist
    @PostLoad
    protected fun load() {
        _isNew = false
    }

    override fun isNew(): Boolean = _isNew
}
