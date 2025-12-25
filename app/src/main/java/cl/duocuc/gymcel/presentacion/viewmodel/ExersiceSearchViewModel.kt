package cl.duocuc.gymcel.presentacion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duocuc.gymcel.data.api.exercise.ExerciseDbApiService
import cl.duocuc.gymcel.data.mapper.toDomain
import cl.duocuc.gymcel.domain.model.Ejercicio
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ExerciseSearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<Ejercicio> = emptyList(),
    val error: String? = null
)

sealed class ExerciseSearchEvent {
    data class OnQueryChanged(val value: String) : ExerciseSearchEvent()
    data class OnExerciseSelected(val ejercicio: Ejercicio) : ExerciseSearchEvent()
    data class OnOpenDetail(val ejercicio: Ejercicio) : ExerciseSearchEvent()
}

class ExerciseSearchViewModel(
    private val api: ExerciseDbApiService
) : ViewModel() {

    // 👇 Esto es SnapshotState, Compose lo observa cuando se lee en @Composable
    var state: ExerciseSearchState by mutableStateOf(ExerciseSearchState())
        private set

    private var searchJob: Job? = null

    fun onEvent(event: ExerciseSearchEvent) {
        when (event) {
            is ExerciseSearchEvent.OnQueryChanged -> {
                state = state.copy(query = event.value)
                debounceSearch(event.value)
            }
            is ExerciseSearchEvent.OnExerciseSelected -> {
                // Nada aquí, lo maneja quien usa este ViewModel
            }
            is ExerciseSearchEvent.OnOpenDetail -> {
                // Igual, solo evento de UI
            }
        }
    }

    private fun debounceSearch(query: String) {
        // Cancelar búsqueda anterior
        searchJob?.cancel()

        // Si la query es muy corta, no molestamos a la API
        if (query.length < 2) {
            state = state.copy(results = emptyList(), isLoading = false, error = null)
            return
        }

        searchJob = viewModelScope.launch {
            delay(350) // debounce
            performSearch(query)
        }
    }

    private suspend fun performSearch(query: String) {
        state = state.copy(isLoading = true, error = null)

        try {
            val response = api.searchExercises(query)

            if (response.isSuccessful) {
                val apiList = response.body()?.data.orEmpty()
                val domainList = apiList.map { it.toDomain() }
                state = state.copy(
                    results = domainList,
                    isLoading = false,
                    error = null
                )
            } else {
                state = state.copy(
                    results = emptyList(),
                    isLoading = false,
                    error = "Error HTTP ${response.code()}"
                )
            }
        } catch (e: Exception) {
            state = state.copy(
                results = emptyList(),
                isLoading = false,
                error = e.message ?: "Error desconocido"
            )
        }
    }
}
