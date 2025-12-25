package cl.duocuc.gymcel.presentacion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duocuc.gymcel.presentacion.ui.components.BottomNavBar
import cl.duocuc.gymcel.presentacion.ui.components.TopNavBar
import cl.duocuc.gymcel.presentacion.viewmodel.DetalleTreinoViewModel
import cl.duocuc.gymcel.presentacion.ui.components.CenteredText
import cl.duocuc.gymcel.presentacion.ui.components.ExerciseCard
import cl.duocuc.gymcel.presentacion.ui.components.UltimoTreinoDialog

@Composable
fun DetalleTreinoScreen(
    navController: NavController,
    treinoId: Long,
    viewModel: DetalleTreinoViewModel = viewModel()
) {
    val rutina by viewModel.rutina.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val seriesUI by viewModel.seriesUI.collectAsState()
    val editable by viewModel.editable.collectAsState()
    val popupData by viewModel.popupUltimoTreino.collectAsState()
    val popupEjercicio by viewModel.popupEjercicio.collectAsState()
    val detallesRutina = viewModel.detallesRutina.collectAsState()
    val nombres by viewModel.nombresEjercicios.collectAsState()

    LaunchedEffect(treinoId) { viewModel.cargarTreino(treinoId) }

    Scaffold(
        topBar = {
            TopNavBar(
                title = rutina?.nombre ?: "Rutina",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            if (editable) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.guardarTreino() },
                    icon = { Icon(Icons.Filled.CheckCircle, contentDescription = null) },
                    text = { Text("Terminar") },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { padding ->

        // --- POPUP ULTIMO TREINO ---
        if (popupData != null && popupEjercicio != null) {
            UltimoTreinoDialog(
                ejercicio = nombres[popupEjercicio!!] ?: popupEjercicio!!,
                series = popupData!!,
                onDismiss = { viewModel.cerrarPopupUltimoTreino() }
            )
        }

        Box(modifier = Modifier.padding(padding)) {
            when {
                loading -> CenteredText("Cargando...")
                rutina == null -> CenteredText("Rutina no encontrada")
                else -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(seriesUI.entries.toList()) { (detalleId, series) ->
                        val detalle = detallesRutina.value.find { it.id == detalleId } ?: return@items
                        LaunchedEffect(detalle.exercise_externalid) {
                            viewModel.cargarNombreEjercicio(detalle.exercise_externalid)
                        }
                        ExerciseCard(
                            exerciseName = nombres[detalle.exercise_externalid] ?: detalle.exercise_externalid,
                            series = series,
                            detalleId = detalleId,
                            editable = editable,
                            onShowLastWorkout = { viewModel.mostrarUltimoTreino(detalle) },
                            onCarga = viewModel::actualizarCarga,
                            onReps = viewModel::actualizarReps,
                            onUnidad = viewModel::actualizarUnidad
                        )
                    }
                }
            }
        }
    }
}
