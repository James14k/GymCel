package cl.duocuc.gymcel.data.local.dao

private val EXCEPTION: UnsupportedOperationException =
    UnsupportedOperationException("Esta operacion no es soportada por no estar implementada. Quien extienda esta clase debe implementar todos los metodos")

abstract class GymcelDao<T> {
    open suspend fun insert(entity: T): Long =
        throw EXCEPTION

    open suspend fun insertAll(entities: List<@JvmSuppressWildcards T>): List<Long> =
        throw EXCEPTION

    open suspend fun update(entity: T): Int =
        throw EXCEPTION

    open suspend fun delete(entity: T): Int =
        throw EXCEPTION

    open suspend fun getById(id: Long): T? =
        throw EXCEPTION

    open suspend fun getAll(): List<@JvmSuppressWildcards T> =
        throw EXCEPTION

    open suspend fun deleteById(id: Long): Int =
        throw EXCEPTION

    open suspend fun count(): Int =
        throw EXCEPTION
}
