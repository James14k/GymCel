package cl.duocuc.gymcel.data.api.exercise

import cl.duocuc.gymcel.data.api.exercise.modelo.ExerciseApiResponse
import cl.duocuc.gymcel.data.api.exercise.modelo.Exercise
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseDbApiService {

    @GET("exercises")
    suspend fun getAllExercises(
        @Query("offset") offset: Int? = 0,
        @Query("limit") limit: Int? = 10,
        @Query("search") search: String? = "",
        @Query("sertBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null
    ): Response<ExerciseApiResponse<List<Exercise>>>

    @GET("exercises/search")
    suspend fun searchExercises(
        @Query("q") q: String,
        @Query("offset") offset: Int? = 0,
        @Query("limit") limit: Int? = 10,
        @Query("treshold") treshHold: Double? = 0.3,
    ): Response<ExerciseApiResponse<List<Exercise>>>

    @GET("exercises/{exerciseId}")
    suspend fun getExerciseById(
        @Path("exerciseId") exerciseId: String
    ): Response<ExerciseApiResponse<Exercise>>


}