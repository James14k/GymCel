package cl.duocuc.gymcel.domain.model

import java.time.DayOfWeek
import java.time.LocalDate

data class TreinoLogUI(
    val treinoId: Long,
    val rutinaNombre: String,
    val fecha: LocalDate,
    val diaSemana: DayOfWeek?,
    val isDone: Boolean
)