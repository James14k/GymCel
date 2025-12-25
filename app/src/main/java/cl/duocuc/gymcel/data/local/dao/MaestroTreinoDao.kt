package cl.duocuc.gymcel.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import cl.duocuc.gymcel.data.local.entities.relations.MaestroTreino

@Dao
abstract class MaestroTreinoDao: GymcelDao<MaestroTreino>() {

    @Transaction
    @Query("SELECT * FROM gmc_treino WHERE id = :id")
    abstract override suspend fun getById(id: Long): MaestroTreino?

    @Transaction
    @Query("SELECT * FROM gmc_treino")
    abstract override suspend fun getAll(): List<MaestroTreino>

}