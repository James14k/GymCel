package cl.duocuc.gymcel.domain.model

enum class UnidadPeso(val gramsFactor: Double, val symbol: String) {
    MILLIGRAM(0.001, "mg"),
    GRAM(1.0, "gr"),
    KILOGRAM(1000.0, "kg"),
    TON(1_000_000.0, "t"),
    POUND(453.59237, "lb"),
    OUNCE(28.349523125, "oz");

    /**
     * Convierte un valor expresado en esta unidad a gramos.
     */
    fun toGrams(value: Double): Double = value * gramsFactor

    /**
     * Convierte un valor en gramos a esta unidad.
     */
    fun fromGrams(grams: Double): Double = grams / gramsFactor

    /**
     * Convierte un valor desde otra unidad hacia esta unidad.
     * Igual a TimeUnit.convert(...)
     */
    fun convert(value: Double, sourceUnit: UnidadPeso): Double {
        val grams = sourceUnit.toGrams(value)
        return fromGrams(grams)
    }
}
