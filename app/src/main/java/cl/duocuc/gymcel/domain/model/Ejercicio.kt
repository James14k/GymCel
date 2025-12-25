package cl.duocuc.gymcel.domain.model

import java.net.URL

data class Ejercicio(
    val id: String,
    val nombre: String,
    val gif: URL? = null,
    val detalle: DetalleEjercicio? = null //1:1
)