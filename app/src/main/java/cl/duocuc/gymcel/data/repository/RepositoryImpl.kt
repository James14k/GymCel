package cl.duocuc.gymcel.data.repository

import cl.duocuc.gymcel.data.local.dao.GymcelDao
import cl.duocuc.gymcel.domain.data.Repository

abstract class BaseRepository<T, ID, DAO :  GymcelDao<T>> (
    protected val dao: DAO
): Repository<T, ID> {

    override suspend fun update(entity: T) {
        dao.update(entity)
    }

    override suspend fun delete(entity: T) {
        dao.delete(entity)
    }

    override suspend fun getAll(): List<T> = dao.getAll()

}

class GymcelRepository<T, DAO : GymcelDao<T>>(
    dao: DAO
) : BaseRepository<T, Long, DAO>(dao) {

    override suspend fun save(entity: T): Long = dao.insert(entity)

    override suspend fun saveAll(entities: List<T>): List<Long> = dao.insertAll(entities)

    override suspend fun getById(id: Long): T? = dao.getById(id)


    override suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }

}

