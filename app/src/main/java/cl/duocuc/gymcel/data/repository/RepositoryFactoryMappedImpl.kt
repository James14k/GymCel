package cl.duocuc.gymcel.data.repository


import cl.duocuc.gymcel.data.local.dao.GymcelDao
import cl.duocuc.gymcel.domain.data.Repository
import cl.duocuc.gymcel.domain.data.RepositoryFactory

class RepositoryFactoryMappedImpl (
    private val registry: Map<Class<*>, () -> GymcelDao<*>>
) : RepositoryFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T, DAO : GymcelDao<T>> create(entityClass: Class<T>): Repository<T, Long> {
        val dao = registry[entityClass]?.invoke()
            ?: IllegalArgumentException("No DAO registrado para ${entityClass.simpleName}")

        return create(dao as DAO)
    }

}