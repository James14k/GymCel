package cl.duocuc.gymcel.data.repository

import cl.duocuc.gymcel.data.local.dao.GymcelDao
import cl.duocuc.gymcel.domain.data.Repository
import cl.duocuc.gymcel.domain.data.RepositoryFactory

class RepositoryFactoryFunctionalImpl(
    private val daoMapper: (Class<*>) ->  GymcelDao<*>?
) : RepositoryFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T, DAO : GymcelDao<T>> create(entityClass: Class<T>): Repository<T, Long> {
        val dao = daoMapper(entityClass)
            ?: throw IllegalArgumentException("No DAO for ${entityClass.simpleName}")

        return create(dao as DAO)
    }

}