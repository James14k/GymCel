package cl.duocuc.gymcel.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import cl.duocuc.gymcel.data.local.entities.*

data class MaestroRutina(
    @Embedded val rutina: RutinaEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "rutina_id",
        entity = ItemRutinaEntity::class
    )
    val entries: List<ItemRutinaEntity>
)

data class MaestroTreino(
    @Embedded
    val treino: TreinoEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "treino_id",
        entity = ItemTreinoEntity::class
    )
    val entries: List<ItemTreinoEntity>
)