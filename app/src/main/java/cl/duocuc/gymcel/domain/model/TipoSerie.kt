package cl.duocuc.gymcel.domain.model

enum class TipoSerie (val desc: String){
    /**
     * Serie clásica: Realizar un número fijo de repeticiones y series con un peso determinado.
     */
    STRAIGHT("serie clásica"),

    /**
     * Comienza con un peso 'pesado' llegando al fallo o a rir1 y luego reduce el pseo para continuar
     */
    DROPSET("dropset"),

    /**
     * Superset: Realizar dos ejercicios consecutivos sin descanso entre ellos.
     * ej; curl de biceps + extenseion de triceps ; descanso ; repetir
     */
    SUPERSET("superset"),

    /**
     * Serie piramidal: Aumentar o disminuir el peso y las repeticiones en cada serie.
     */
    PYRAMID_ASC("piramidal ascendente"),

    /**
     * Serie piramidal: Aumentar o disminuir el peso y las repeticiones en cada serie.
     */
    PYRAMID_DESC("piramidal descendente"),

    /**
     * Rest-pause: Realizar una serie hasta el fallo, descansar brevemente y luego continuar con más repeticiones.
     * ej; vuelos laterales rir 0 ; 15 seg descanso ; vuelos laterales rir 1; descando ; repetir
     */
    REST_PAUSE("rest-pause"),

    /**
     * el nombre lo dice
     * as many reps as posible (tantas repeticiones como sea posible)
     * suele implementarse en rutinas de crossfit o entrenamiento funcional
     */
    AMRAP("amrap")
}
