package model.datasource.dao

interface DAO<E, K> {
    fun create(entity: E): java.io.Serializable?
    fun read(key: K): E
    fun read(first: Int, last: Int): List<E>
    fun update(entity: E)
    fun delete(entity: E )
    fun saveOrUpdate(entity: E)
}