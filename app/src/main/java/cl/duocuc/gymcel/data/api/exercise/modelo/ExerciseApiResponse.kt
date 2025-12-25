package cl.duocuc.gymcel.data.api.exercise.modelo

import java.net.URL

open class ExerciseApiResponse<T>(
    val success: Boolean,
    val metadata: ExerciseApiMetaData?,
    val data: T
)

data class ExerciseApiMetaData(
    val totalExercises: Int,
    val totalPages: Int,
    val currentPage: Int,
    val previousPage: URL?,
    val nextPage: URL?,
)