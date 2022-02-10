package model.datasource.dao

import org.hibernate.Session
import org.hibernate.SessionFactory
import java.io.Serializable

class UniversalDAO<D, K : Serializable>(
    private val factory: SessionFactory,
    private val entityClass: Class<D>
) : DAO<D, K> {

    override fun create(entity: D) {
        withSession {
            it.save(entity)
        }
    }

    override fun read(key: K): D = withSession(false) {
        it.get(entityClass, key)
    }

    override fun update(entity: D) {
        withSession {
            it.update(entity)
        }
    }

    override fun saveOrUpdate(entity: D) {
        withSession {
            it.saveOrUpdate(entity)
        }
    }

    override fun delete(entity: D) {
        withSession {
            it.delete(entity)
        }
    }

    private fun <T> withSession(
        beginTransaction: Boolean = true,
        block: (Session) -> T
    ): T {
        val session = factory.openSession()

        if (beginTransaction) session.beginTransaction()

        return block(session).also {
            if (beginTransaction) session.transaction.commit()
        }
    }
}