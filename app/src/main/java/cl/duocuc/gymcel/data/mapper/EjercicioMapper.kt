package cl.duocuc.gymcel.data.mapper

import cl.duocuc.gymcel.data.api.exercise.modelo.Exercise
import cl.duocuc.gymcel.domain.model.DetalleEjercicio
import cl.duocuc.gymcel.domain.model.Ejercicio

fun Exercise.toDomain(): Ejercicio = Ejercicio(
    id = exerciseId,
    nombre = name,
    gif = gifUrl,
    detalle = DetalleEjercicio(
        musculosTrabajados =  targetMuscles,
        equipamiento = equipments,
        instrucciones = instructions
    )
)


//no hay toEntity porque la api es solo lectura...