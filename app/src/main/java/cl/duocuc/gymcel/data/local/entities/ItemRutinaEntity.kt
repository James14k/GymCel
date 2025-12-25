package cl.duocuc.gymcel.data.local.entities

import androidx.room.*

@Entity(
    tableName = "gmc_itemrutina",
    foreignKeys = [
        ForeignKey(
            entity = RutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["rutina_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("rutina_id")]
)
data class ItemRutinaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rutina_id: Long,
    val exercise_externalid: String,
    val order_index: Int = 0,
    val sets_amount: Int,
    val reps_goal: Int?,
    val reps_range_min: Int?,
    val reps_range_max: Int?,
    val variation: String,
)