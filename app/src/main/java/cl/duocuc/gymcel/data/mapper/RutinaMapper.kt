package cl.duocuc.gymcel.data.mapper

import cl.duocuc.gymcel.data.local.entities.ItemRutinaEntity
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import cl.duocuc.gymcel.data.local.entities.relations.MaestroRutina
import cl.duocuc.gymcel.domain.model.DetalleRutina
import cl.duocuc.gymcel.domain.model.Rutina
import cl.duocuc.gymcel.domain.model.TipoSerie
import java.time.DayOfWeek

fun RutinaEntity.toDomain(): Rutina = Rutina(
    id = id,
    nombre = name,
    descripcion = desc,
    dia = dia?.let { DayOfWeek.valueOf(it) }
)

fun Rutina.toEntity(): RutinaEntity = RutinaEntity(
    id = id,
    name = nombre,
    desc = descripcion,
    dia = dia?.name
)


fun ItemRutinaEntity.toDomain(): DetalleRutina {
    val rango = if (reps_range_min != null && reps_range_max != null) {
        reps_range_min..reps_range_max
    } else {
        null
    }

    val tipo = runCatching { TipoSerie.valueOf(variation) }
        .getOrElse { TipoSerie.STRAIGHT }

    return DetalleRutina(
        id = id,
        ejercicioId = exercise_externalid,
        orden = order_index,
        series = sets_amount,
        objetivoReps = reps_goal,
        rangoReps = rango,
        tipoSerie = tipo
    )
}

fun DetalleRutina.toEntity(rutinaId: Long): ItemRutinaEntity = ItemRutinaEntity(
    id = id,
    rutina_id = rutinaId,
    exercise_externalid = ejercicioId?: "",
    order_index = orden,
    sets_amount = series,
    reps_goal = objetivoReps,
    reps_range_min = rangoReps?.first,
    reps_range_max = rangoReps?.last,
    variation = tipoSerie?.name ?: TipoSerie.STRAIGHT.name
)

fun MaestroRutina.toDomain(): Rutina = rutina.toDomain().copy(
    detalles = entries.sortedBy { it.order_index }.map { it.toDomain() }
)

fun Rutina.toMasterEntity(): MaestroRutina = MaestroRutina(
    rutina = this.toEntity(),
    entries = this.detalles?.map { it.toEntity(this.id) } ?: emptyList()
)