package cl.duocuc.gymcel.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import cl.duocuc.gymcel.data.local.entities.relations.MaestroRutina

@Dao
abstract class MaestroRutinaDao: GymcelDao<MaestroRutina>() {

    @Transaction
    @Query("SELECT * FROM gmc_rutina WHERE id = :id")
    abstract override suspend fun getById(id: Long): MaestroRutina?

    @Transaction
    @Query("SELECT * FROM gmc_rutina")
    abstract override suspend fun getAll(): List<@JvmSuppressWildcards MaestroRutina>

}