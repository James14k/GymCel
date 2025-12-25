package cl.duocuc.gymcel

import android.content.Context
import androidx.room.Room
import cl.duocuc.gymcel.data.api.exercise.ExerciseDbApiService
import cl.duocuc.gymcel.data.api.placeholder.JSONPhApiService
import cl.duocuc.gymcel.data.local.db.GymDatabase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

object AppConstants {


    @Volatile
    private var dbInstance: GymDatabase? = null

    fun getDatabase(context: Context): GymDatabase =
        dbInstance ?: synchronized(this) {
            dbInstance ?: Room.databaseBuilder(
                context.applicationContext,
                GymDatabase::class.java,
                "gymcel.db"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { dbInstance = it }
        }

    object Api {

        private val okHttpClient: OkHttpClient by lazy {
            OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                .build()
        }

        private fun retrofit(baseUrl: String): Retrofit =
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        private fun <T : Any> createApi(
            baseUrl: String,
            service: KClass<T>
        ): T =
            retrofit(baseUrl).create(service.java)


        private const val EXERCISEDB_BASE_URL = "https://www.exercisedb.dev/api/v1/"
        private const val JSONPLACEHOLDER_BASE_URL = "https://jsonplaceholder.typicode.com/"

        @Volatile
        private var exerciseDbApi: ExerciseDbApiService? = null

        @Volatile
        private var jsonPlaceholderApi: JSONPhApiService? = null

        fun exerciseDb(): ExerciseDbApiService =
            exerciseDbApi ?: synchronized(this) {
                exerciseDbApi ?: createApi(
                    EXERCISEDB_BASE_URL,
                    ExerciseDbApiService::class
                ).also { exerciseDbApi = it }
            }

        fun jsonPlaceholder(): JSONPhApiService =
            jsonPlaceholderApi ?: synchronized(this) {
                jsonPlaceholderApi ?: createApi(
                    JSONPLACEHOLDER_BASE_URL,
                    JSONPhApiService::class
                ).also { jsonPlaceholderApi = it }
            }


    }


    object StateKeys {
        const val EJERCICIO_SEL = "slctd_xrsice"
    }
}
