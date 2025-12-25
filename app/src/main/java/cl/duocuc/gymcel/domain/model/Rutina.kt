package cl.duocuc.gymcel.domain.model

import java.time.DayOfWeek

data class Rutina(
    val id: Long,
    val nombre: String,
    val descripcion: String?,
    val dia: DayOfWeek? = null,
    val detalles: List<DetalleRutina>? = null //1:N
)
