package cl.duocuc.gymcel.domain.model

data class Peso(
    val value: Double,
    val unit: UnidadPeso
) {

    //TODO: implementar preferencias de usuarios, para sacar si prefiere wea gringa o wea europea
    constructor(value: Double) : this(value, UnidadPeso.KILOGRAM)

    fun grams(): Peso {
        return to(UnidadPeso.GRAM)
    }

    fun to(targetUnit: UnidadPeso): Peso {
        val newValue = targetUnit.convert(value, unit)
        return Peso(newValue, targetUnit)
    }

    override fun toString(): String = "$value ${unit.name.lowercase()}"
}
