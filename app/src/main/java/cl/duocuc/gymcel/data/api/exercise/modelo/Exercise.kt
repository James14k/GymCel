package cl.duocuc.gymcel.data.api.exercise.modelo

import java.net.URL

data class Exercise(
    val exerciseId: String,
    val name: String,
    val gifUrl: URL,
    val targetMuscles: List<String>,
    val bodyParts: List<String>,
    val equipments: List<String>,
    val secondaryMuscles: List<String>,
    val instructions: List<String>
)