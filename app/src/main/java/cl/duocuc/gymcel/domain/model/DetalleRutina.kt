package cl.duocuc.gymcel.domain.model

data class DetalleRutina(
    val id: Long,
    val ejercicioId: String? = null,
    val orden: Int,
    val series: Int,
    val objetivoReps: Int?,
    val rangoReps: IntRange?,
    val tipoSerie: TipoSerie? = TipoSerie.STRAIGHT
) {
    fun isRange(): Boolean = rangoReps != null && rangoReps.first < rangoReps.last
}