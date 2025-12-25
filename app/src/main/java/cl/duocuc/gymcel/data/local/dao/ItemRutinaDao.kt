package cl.duocuc.gymcel.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cl.duocuc.gymcel.data.local.entities.ItemRutinaEntity

@Dao
abstract class ItemRutinaDao : GymcelDao<ItemRutinaEntity>() {

    @Insert
    abstract
    override suspend fun insert(entity: ItemRutinaEntity): Long

    @Insert
    abstract
    override suspend fun insertAll(entities: List<ItemRutinaEntity>): List<Long>

    @Update
    abstract
    override suspend fun update(entity: ItemRutinaEntity): Int

    @Delete
    abstract
    override suspend fun delete(entity: ItemRutinaEntity): Int

    @Query("SELECT * FROM gmc_itemrutina WHERE id = :id")
    abstract
    override suspend fun getById(id: Long): ItemRutinaEntity?

    @Query("SELECT * FROM gmc_itemrutina")
    abstract
    override suspend fun getAll(): List<ItemRutinaEntity>

    @Query("DELETE FROM gmc_itemrutina WHERE id = :id")
    abstract
    override suspend fun deleteById(id: Long): Int

    @Query("SELECT COUNT(*) FROM gmc_itemrutina")
    abstract
    override suspend fun count(): Int
    
}