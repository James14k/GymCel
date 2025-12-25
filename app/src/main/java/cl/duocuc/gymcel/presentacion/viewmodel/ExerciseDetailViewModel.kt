package cl.duocuc.gymcel.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duocuc.gymcel.data.api.exercise.ExerciseDbApiService
import cl.duocuc.gymcel.data.mapper.toDomain
import cl.duocuc.gymcel.domain.model.Ejercicio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ExerciseDetailUiState {
    object Loading : ExerciseDetailUiState()
    data class Success(val ejercicio: Ejercicio) : ExerciseDetailUiState()
    data class Error(val mensaje: String) : ExerciseDetailUiState()
}


class ExerciseDetailViewModel(
    private val api: ExerciseDbApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExerciseDetailUiState>(ExerciseDetailUiState.Loading)
    val uiState: StateFlow<ExerciseDetailUiState> = _uiState

    fun loadExercise(exerciseId: String) {
        viewModelScope.launch {
            _uiState.value = ExerciseDetailUiState.Loading

            try {
                val response = api.getExerciseById(exerciseId)

                if (response.isSuccessful) {
                    val body = response.body()?.data

                    if (body != null) {
                        _uiState.value = ExerciseDetailUiState.Success(body.toDomain())
                    } else {
                        _uiState.value = ExerciseDetailUiState.Error("Respuesta vacía")
                    }
                } else {
                    _uiState.value = ExerciseDetailUiState.Error(
                        "Error HTTP: ${response.code()}"
                    )
                }

            } catch (ex: Exception) {
                _uiState.value = ExerciseDetailUiState.Error(ex.message ?: "Error desconocido")
            }
        }
    }
}
