package cl.duocuc.gymcel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.duocuc.gymcel.data.local.entities.RutinaEntity


@Dao
abstract class RutinaDao : GymcelDao<RutinaEntity>() {

    @Insert
    abstract override suspend fun insert(entity: RutinaEntity): Long

    @Insert
    abstract override suspend fun insertAll(entities: List<RutinaEntity>): List<Long>

    @Update
    abstract override suspend fun update(entity: RutinaEntity): Int

    @Delete
    abstract override suspend fun delete(entity: RutinaEntity): Int

    @Query("SELECT * FROM gmc_rutina WHERE id = :id")
    abstract override suspend fun getById(id: Long): RutinaEntity?

    @Query("SELECT * FROM gmc_rutina")
    abstract override suspend fun getAll(): List<RutinaEntity>

    @Query("DELETE FROM gmc_rutina WHERE id = :id")
    abstract override suspend fun deleteById(id: Long): Int

    @Query("SELECT COUNT(*) FROM gmc_rutina")
    abstract override suspend fun count(): Int


}
