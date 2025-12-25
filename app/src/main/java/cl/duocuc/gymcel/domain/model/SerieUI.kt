package cl.duocuc.gymcel.domain.model

data class SerieUI(
    val numero: Int,
    val carga: Double,
    val reps: Int,
    val unidad: UnidadPeso,
    val meta: String?,
    val editable: Boolean
)

