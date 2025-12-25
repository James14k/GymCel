package cl.duocuc.gymcel.presentacion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duocuc.gymcel.presentacion.ui.components.CenteredText
import cl.duocuc.gymcel.presentacion.ui.components.DetalleEjercicio
import cl.duocuc.gymcel.presentacion.ui.components.TopNavBar
import cl.duocuc.gymcel.presentacion.viewmodel.ExerciseDetailUiState
import cl.duocuc.gymcel.presentacion.viewmodel.ExerciseDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    viewModel: ExerciseDetailViewModel,
    exerciseId: String?,
    onSelect: (String) -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val state by viewModel.uiState.collectAsState()

    if (exerciseId == null) {
        CenteredText("nada que ver aqui...")
        return;
    }

    LaunchedEffect(exerciseId) {
        viewModel.loadExercise(exerciseId)
    }

    Scaffold(
        topBar = {
            TopNavBar(
                showBackButton = onBackClick != null,
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            if (state is ExerciseDetailUiState.Success) {
                FloatingActionButton(
                    onClick = {
                        val ejercicio = (state as ExerciseDetailUiState.Success).ejercicio
                        onSelect(ejercicio.id)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Seleccionar ejercicio"
                    )
                }
            }
        }
    ) { paddingValues ->

        when (state) {

            ExerciseDetailUiState.Loading -> {
                Text(
                    "Cargando...",
                    modifier = Modifier.padding(paddingValues).padding(16.dp)
                )
            }

            is ExerciseDetailUiState.Error -> {
                Text(
                    "Error: ${(state as ExerciseDetailUiState.Error).mensaje}",
                    modifier = Modifier.padding(paddingValues).padding(16.dp)
                )
            }

            is ExerciseDetailUiState.Success -> {
                val ejercicio = (state as ExerciseDetailUiState.Success).ejercicio

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    DetalleEjercicio(
                        ejercicio = ejercicio,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
