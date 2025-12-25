package cl.duocuc.gymcel.data


import cl.duocuc.gymcel.data.local.dao.DaoFactory
import cl.duocuc.gymcel.data.local.dao.DaoFactoryImpl
import cl.duocuc.gymcel.data.local.dao.GymcelDao
import cl.duocuc.gymcel.data.local.db.GymDatabase
import cl.duocuc.gymcel.data.local.entities.*
import cl.duocuc.gymcel.data.local.entities.relations.MaestroRutina
import cl.duocuc.gymcel.data.local.entities.relations.MaestroTreino
import cl.duocuc.gymcel.data.repository.RepositoryFactoryFunctionalImpl
import cl.duocuc.gymcel.data.repository.RepositoryFactoryMappedImpl
import cl.duocuc.gymcel.domain.data.RepositoryFactory

object FactoryProvider {

    // Nunca nulo. Si no ha sido inicializado, se lanza error explícito.
    private lateinit var cache: Map<Class<*>, () -> GymcelDao<*>>

    /** Inicializa el registro y lo retorna. */
    fun registry(db: GymDatabase): Map<Class<*>, () -> GymcelDao<*>> {
        if (!::cache.isInitialized) {
            cache = mapOf(
                RutinaEntity::class.java to { db.rutinaDao() },
                ItemRutinaEntity::class.java to { db.itemRutinaDao() },
                TreinoEntity::class.java to { db.treinoDao() },
                ItemTreinoEntity::class.java to { db.itemTreinoDao() },
                MaestroRutina::class.java to { db.maestroRutinaDao() },
                MaestroTreino::class.java to { db.maestroTreinoDao() }
            )
        }
        return cache
    }



    fun daoFactory(registry: Map<Class<*>, () -> GymcelDao<*>>): DaoFactory =
        DaoFactoryImpl(registry)


    /** RepositoryFactory basado en un mapa directo */
    fun repositoryFactory(registry: Map<Class<*>, () -> GymcelDao<*>>): RepositoryFactory =
        RepositoryFactoryMappedImpl(registry)

    /** RepositoryFactory basado en un DaoFactory */
    fun repositoryFactory(daoFactory: DaoFactory): RepositoryFactory =
        repositoryFactory(daoFactory::create)

    /** RepositoryFactory basado en una lambda mapper */
    fun repositoryFactory(daoMapper: (Class<*>) -> GymcelDao<*>?): RepositoryFactory =
        RepositoryFactoryFunctionalImpl(daoMapper)


}
