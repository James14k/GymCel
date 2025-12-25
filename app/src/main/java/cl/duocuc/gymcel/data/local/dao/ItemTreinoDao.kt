package cl.duocuc.gymcel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.duocuc.gymcel.data.local.entities.ItemTreinoEntity

@Dao
abstract class ItemTreinoDao : GymcelDao<ItemTreinoEntity>() {

    @Insert
    abstract override suspend fun insert(entity: ItemTreinoEntity): Long

    @Insert
    abstract override suspend fun insertAll(entities: List<ItemTreinoEntity>): List<Long>

    @Update
    abstract override suspend fun update(entity: ItemTreinoEntity): Int

    @Delete
    abstract override suspend fun delete(entity: ItemTreinoEntity): Int

    @Query("SELECT * FROM gmc_itemtreino WHERE id = :id")
    abstract override suspend fun getById(id: Long): ItemTreinoEntity?

    @Query("SELECT * FROM gmc_itemtreino")
    abstract override suspend fun getAll(): List<ItemTreinoEntity>

    @Query("DELETE FROM gmc_itemtreino WHERE id = :id")
    abstract override suspend fun deleteById(id: Long): Int

    @Query("SELECT COUNT(*) FROM gmc_itemtreino")
    abstract override suspend fun count(): Int

    @Query("""
    SELECT T1.* FROM gmc_itemtreino AS T1
    INNER JOIN gmc_treino AS T2 
        ON T1.treino_id = T2.id
    WHERE T1.exercise_externalid = :exerciseId
        AND T1.treino_id = (
            SELECT T3.id 
            FROM gmc_treino AS T3 
            WHERE 
                T3.done = 1               
                AND T3.rutina_id = :rutinaId 
            ORDER BY T3.timestamp DESC 
            LIMIT 1
        )
    ORDER BY T1.id ASC
""")
    abstract suspend fun getUltimoPorEjercicio(exerciseId: String, rutinaId: Long): List<ItemTreinoEntity>

}