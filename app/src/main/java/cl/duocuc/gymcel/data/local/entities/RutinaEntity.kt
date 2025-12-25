package cl.duocuc.gymcel.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gmc_rutina")
data class RutinaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val desc: String?,
    val dia: String? = null
)