package gachi.chills.global.base

import gachi.chills.global.util.IdGenerator
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class HardDeleteUuidBaseJpaEntity : Persistable<String> {
    @Id
    @get:JvmName("id")
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(32)")
    val id: String = IdGenerator.uuid()

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set

    override fun getId(): String? = id

    @Transient
    private var _isNew: Boolean = true

    @PostPersist
    @PostLoad
    protected fun load() {
        _isNew = false
    }

    override fun isNew(): Boolean = _isNew
}
