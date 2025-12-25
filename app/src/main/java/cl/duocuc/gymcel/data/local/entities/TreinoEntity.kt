package cl.duocuc.gymcel.data.local.entities

import androidx.room.*
import java.time.Instant

@Entity(
    tableName = "gmc_treino",
    foreignKeys = [
        ForeignKey(
            entity = RutinaEntity::class,
            parentColumns = ["id"],
            childColumns = ["rutina_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("rutina_id")]
)
data class TreinoEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rutina_id: Long,
    val timestamp: Long = Instant.now().toEpochMilli(),
    val done: Boolean = false,
    val notas: String?
)