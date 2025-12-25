package cl.duocuc.gymcel.data.mapper

import cl.duocuc.gymcel.data.local.entities.ItemTreinoEntity
import cl.duocuc.gymcel.data.local.entities.TreinoEntity
import cl.duocuc.gymcel.data.local.entities.relations.MaestroTreino
import cl.duocuc.gymcel.domain.model.DetalleTreino
import cl.duocuc.gymcel.domain.model.Ejercicio
import cl.duocuc.gymcel.domain.model.Peso
import cl.duocuc.gymcel.domain.model.Treino
import cl.duocuc.gymcel.domain.model.UnidadPeso
import java.time.Instant
import kotlin.time.Duration.Companion.nanoseconds

fun TreinoEntity.toDomain(): Treino = Treino(
    id = id,
    timestamp = Instant.ofEpochMilli(timestamp),
    done = done,
    notas = notas
)

fun Treino.toEntity(): TreinoEntity = TreinoEntity(
    id = id,
    rutina_id = rutina?.id ?: throw IllegalArgumentException("de proveer un id de rutina"),
    timestamp = timestamp.toEpochMilli(),
    done = done,
    notas = notas
)

fun ItemTreinoEntity.toDomain(): DetalleTreino = toDomain { null }

fun ItemTreinoEntity.toDomain(
    ejercicioMapper: (String) -> Ejercicio?
): DetalleTreino = DetalleTreino(
    id = id,
    ejercicio = ejercicioMapper(exercise_externalid),
    repeticionesEfectivas = effective_reps,
    cargaEfectiva = Peso(
        value = effective_load,
        unit = UnidadPeso.valueOf(load_unit)
    ),
    rir = rir,
    descanso = rest_nanos?.nanoseconds
)


fun DetalleTreino.toEntity(treinoId: Long): ItemTreinoEntity = ItemTreinoEntity(
    id = id,
    treino_id = treinoId,
    exercise_externalid = ejercicio?.id?: throw IllegalArgumentException("de proveer un id de ejercicio"),
    effective_reps = repeticionesEfectivas,
    effective_load = cargaEfectiva.value,
    load_unit = cargaEfectiva.unit.name,
    rir = rir,
    rest_nanos = descanso?.inWholeNanoseconds
)

fun MaestroTreino.toDomain(): Treino = treino.toDomain().copy(
    detalles = entries.sortedBy { it.id }.map { it.toDomain() }
)

fun MaestroTreino.toDomain(
    ejercicioMapper: (String) -> Ejercicio?
): Treino = treino.toDomain().copy(
    detalles = entries.sortedBy { it.id }.map { it.toDomain(ejercicioMapper) }
)

fun Treino.toMasterEntity(): MaestroTreino = MaestroTreino(
    treino = this.toEntity(),
    entries = this.detalles?.map { it.toEntity(this.id) } ?: emptyList()
)