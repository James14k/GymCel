package cl.duocuc.gymcel.domain.data

interface ControlEntidadMaestra<T, ID> {

    suspend fun load(id: ID): T?

    suspend fun loadAll(): List<T>

    suspend fun save(aggregate: T): ID

    suspend fun saveAll(aggregates: List<T>): List<ID>

    suspend fun delete(aggregate: T)

}

interface GymcelControlEntidadMaestra<T>: ControlEntidadMaestra<T, Long>

