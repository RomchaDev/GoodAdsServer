package model.datasource.dao

import org.hibernate.Session
import org.hibernate.SessionFactory
import java.io.Serializable
import javax.persistence.criteria.CriteriaBuilder


class UniversalDAO<D, K : Serializable>(
    private val factory: SessionFactory,
    private val entityClass: Class<D>
) : DAO<D, K> {

    override fun create(entity: D) = try {
        withSession {
            it.save(entity)
        }
    } catch (e: Exception) {
        null
    }

    override fun read(key: K): D = withSession(false) {
        it.get(entityClass, key)
    }

    override fun read(first: Int, last: Int): List<D> =
        withSession(false) { s ->
            val cb: CriteriaBuilder = s.criteriaBuilder
            val cq = cb.createQuery(entityClass)
            val rootEntry = cq.from(entityClass)
            val all = cq.select(rootEntry)
            val allQuery = s.createQuery(all)
            allQuery.firstResult = first
            allQuery.maxResults = last - first
            allQuery.resultList
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
            session.close()
        }
    }
}