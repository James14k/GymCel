package cl.duocuc.gymcel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.duocuc.gymcel.data.local.entities.TreinoEntity

@Dao
abstract class TreinoDao : GymcelDao<TreinoEntity>() {

    @Insert
    abstract override suspend fun insert(entity: TreinoEntity): Long

    @Insert
    abstract override suspend fun insertAll(entities: List<TreinoEntity>): List<Long>

    @Update
    abstract override suspend fun update(entity: TreinoEntity): Int

    @Delete
    abstract override suspend fun delete(entity: TreinoEntity): Int

    @Query("SELECT * FROM gmc_treino WHERE id = :id")
    abstract override suspend fun getById(id: Long): TreinoEntity?

    @Query("SELECT * FROM gmc_treino")
    abstract override suspend fun getAll(): List<TreinoEntity>

    @Query("DELETE FROM gmc_treino WHERE id = :id")
    abstract override suspend fun deleteById(id: Long): Int

    @Query("SELECT COUNT(*) FROM gmc_treino")
    abstract override suspend fun count(): Int

}