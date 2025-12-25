package cl.duocuc.gymcel.domain.model

data class DetalleEjercicio(
    val musculosTrabajados: List<String>,
    val equipamiento: List<String>,
    val instrucciones: List<String>
)