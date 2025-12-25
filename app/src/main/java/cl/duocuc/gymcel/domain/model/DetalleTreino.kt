package cl.duocuc.gymcel.domain.model

import kotlin.time.Duration

data class DetalleTreino(
    val id: Long,
    val ejercicio: Ejercicio?,
    val repeticionesEfectivas: Int,
    val cargaEfectiva: Peso,
    val rir: Int?,
    val descanso: Duration? = null
)