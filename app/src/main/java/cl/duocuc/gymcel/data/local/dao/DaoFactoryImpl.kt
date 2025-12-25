package cl.duocuc.gymcel.data.local.dao


class DaoFactoryImpl(
    private val registry: Map<Class<*>, () -> GymcelDao<*>>
) : DaoFactory {

    @Suppress("UNCHECKED_CAST")
    override fun <T :  GymcelDao<*>> create(entityClass: Class<*>): T {
        return registry[entityClass]?.invoke() as? T
            ?: throw IllegalArgumentException("No DAO for ${entityClass.simpleName}")
    }



}