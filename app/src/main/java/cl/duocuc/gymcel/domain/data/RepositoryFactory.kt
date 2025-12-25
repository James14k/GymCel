package cl.duocuc.gymcel.domain.data

import cl.duocuc.gymcel.data.local.dao.GymcelDao
import cl.duocuc.gymcel.data.repository.GymcelRepository

interface RepositoryFactory {

    fun <T, DAO: GymcelDao<T> > create(entityClass: Class<T>): Repository<T, Long>

    fun <T, DAO: GymcelDao<T>> create(dao: DAO) : Repository<T, Long> = GymcelRepository(dao)

}