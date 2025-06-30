package gachi.chills.domain.user.domain.model

import gachi.chills.global.base.HardDeleteUuidBaseJpaEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    name: String,
    email: String,
) : HardDeleteUuidBaseJpaEntity() {
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(30)")
    var name: String = name
        private set

    @Column(name = "email", nullable = false, columnDefinition = "VARCHAR(50)")
    var email: String = email
        private set

    companion object {
        fun signUp(
            name: String,
            email: String,
        ): User {
            return User(
                name = name,
                email = email,
            )
        }
    }
}
