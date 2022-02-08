package model.dao

interface DAO<E, K> {
    fun create(entity: E)
    fun read(key: K): E
    fun update(entity: E)
    fun delete(entity: E )
}