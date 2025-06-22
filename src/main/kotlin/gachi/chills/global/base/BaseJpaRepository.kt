package gachi.chills.global.base

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseJpaRepository<T, ID> : JpaRepository<T, ID> {
    @Deprecated(
        message = "Use findByIdOrThrow instead.",
        replaceWith = ReplaceWith("this.findByIdOrThrow(id)"),
    )
    override fun getById(id: ID & Any): T & Any {
        throw UnsupportedOperationException()
    }

    @Deprecated(
        message = "Use findByIdOrThrow instead.",
        replaceWith = ReplaceWith("this.findByIdOrThrow(id)"),
    )
    override fun getReferenceById(id: ID & Any): T & Any {
        throw UnsupportedOperationException()
    }
}
