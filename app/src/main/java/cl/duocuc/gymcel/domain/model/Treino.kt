package cl.duocuc.gymcel.domain.model

import java.time.Instant

data class Treino(
    val id: Long,
    val rutina: Rutina? = null,
    val timestamp: Instant,
    val done: Boolean = false,
    val notas: String? = null,
    val detalles: List<DetalleTreino>? = null //1:N
)