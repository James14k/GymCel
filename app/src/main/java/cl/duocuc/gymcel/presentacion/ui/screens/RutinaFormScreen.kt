package cl.duocuc.gymcel.presentacion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duocuc.gymcel.AppConstants
import cl.duocuc.gymcel.AppRoutes
import cl.duocuc.gymcel.domain.model.DetalleRutina
import cl.duocuc.gymcel.domain.model.Ejercicio
import cl.duocuc.gymcel.domain.model.TipoSerie
import cl.duocuc.gymcel.presentacion.ui.components.BottomNavBar
import cl.duocuc.gymcel.presentacion.ui.components.DetalleRutinaFormCard
import cl.duocuc.gymcel.presentacion.ui.components.TopNavBar
import cl.duocuc.gymcel.presentacion.ui.components.UnderlineTextField
import cl.duocuc.gymcel.presentacion.viewmodel.RutinaDetalleItemUi
import cl.duocuc.gymcel.presentacion.viewmodel.RutinaFormUiState
import cl.duocuc.gymcel.presentacion.viewmodel.RutinaFormViewModel
import cl.duocuc.gymcel.presentacion.viewmodel.RutinaHeaderUi
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaFormScreen(
    viewModel: RutinaFormViewModel,
    navController: NavController
) {
    val savedStateHandle = navController.currentBackStackEntry
        ?.savedStateHandle

    val ejercicioSeleccionado =
        savedStateHandle?.get<String>(AppConstants.StateKeys.EJERCICIO_SEL)

    if (ejercicioSeleccionado != null) {
        viewModel.addDetalleForExercise(ejercicioSeleccionado)
        savedStateHandle.remove<String>(AppConstants.StateKeys.EJERCICIO_SEL)
    }

    Scaffold(
        topBar = { TopNavBar() },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            RutinaForm(
                state = viewModel.uiState.collectAsState().value,
                onNombreChange = viewModel::onNombreChange,
                onDescripcionChange = viewModel::onDescripcionChange,
                onDiaChange = viewModel::onDiaChange,
                onDetalleChange = viewModel::onDetalleChanged,
                onRemoveDetalle = viewModel::onRemoveDetalle,

                onAddDetalleClick = {
                    navController.navigate(AppRoutes.BUSCAR_EJERCICIO())
                },

                onGuardarClick = {
                    viewModel.onGuardarClick()
                    navController.popBackStack()
                }
            )

        }
    }

}


/**
 * Formulario visual de Rutina. Es un componente puro, sin ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinaForm(
    state: RutinaFormUiState,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onDiaChange: (DayOfWeek?) -> Unit,
    onDetalleChange: (localId: Long, DetalleRutina) -> Unit,
    onRemoveDetalle: (localId: Long) -> Unit,
    onAddDetalleClick: () -> Unit,
    onGuardarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDetalleClick,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar ejercicio a la rutina",
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Nueva rutina",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = MaterialTheme.typography.headlineSmall.fontWeight
            )

            UnderlineTextField(
                value = state.header.nombre,
                onValueChange = onNombreChange,
                label = "Nombre de la rutina",
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.header.descripcion,
                onValueChange = onDescripcionChange,
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )

            var expandedDia by remember { mutableStateOf(false) }

            val localeEs = remember { Locale("es", "ES") }

            val diaSeleccionadoLabel = state.header.dia
                ?.getDisplayName(TextStyle.FULL, localeEs)
                ?.replaceFirstChar { it.uppercase(localeEs) }
                ?: "Cualquiera"

            ExposedDropdownMenuBox(
                expanded = expandedDia,
                onExpandedChange = { expandedDia = !expandedDia }
            ) {
                OutlinedTextField(
                    value = diaSeleccionadoLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Día (opcional)") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDia)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedDia,
                    onDismissRequest = { expandedDia = false }
                ) {

                    // Opción por defecto
                    DropdownMenuItem(
                        text = { Text("Cualquiera") },
                        onClick = {
                            onDiaChange(null)
                            expandedDia = false
                        }
                    )

                    Divider()

                    DayOfWeek.entries.forEach { dia ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    dia.getDisplayName(TextStyle.FULL, localeEs)
                                        .replaceFirstChar { it.uppercase(localeEs) }
                                )
                            },
                            onClick = {
                                onDiaChange(dia)
                                expandedDia = false
                            }
                        )
                    }
                }
            }



            Divider()


            Text(
                text = "Ejercicios de la rutina",
                style = MaterialTheme.typography.titleMedium
            )

            if (state.detalles.isEmpty()) {
                Text(
                    text = "Aún no has agregado ejercicios. Usa el botón + para añadir.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                state.detalles
                    .sortedBy { it.detalle.orden }
                    .forEach { item ->
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DetalleRutinaFormCard(
                                ejercicioNombre = item.ejercicioNombre,
                                ejercicioId = item.ejercicioId,
                                ordenInferido = item.detalle.orden,
                                initial = item.detalle,
                                onValueChange = { updated ->
                                    onDetalleChange(item.localId, updated)
                                }
                            )

                            IconButton(
                                onClick = { onRemoveDetalle(item.localId) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Quitar ejercicio"
                                )
                            }
                        }
                    }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }


            Button(
                onClick = onGuardarClick,
                enabled = state.canSave && !state.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (state.isSaving) "Guardando..." else "Guardar rutina"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RutinaFormPreview() {
    val dummyDetalle = DetalleRutina(
        id = 0L,
        ejercicioId = "bench_press_001",
        orden = 1,
        series = 3,
        objetivoReps = 10,
        rangoReps = null,
        tipoSerie = TipoSerie.STRAIGHT
    )

    val uiState = RutinaFormUiState(
        header = RutinaHeaderUi(
            nombre = "Pecho pesado",
            descripcion = "Rutina pesada de pecho para lunes",
            dia = DayOfWeek.MONDAY
        ),
        detalles = listOf(
            RutinaDetalleItemUi(
                localId = 1L,
                ejercicioId = "bench_press_001",
                ejercicioNombre = "Press banca plano",
                detalle = dummyDetalle
            )
        ),
        canSave = true
    )

    MaterialTheme {
        RutinaForm(
            state = uiState,
            onNombreChange = {},
            onDescripcionChange = {},
            onDiaChange = {},
            onDetalleChange = { _, _ -> },
            onRemoveDetalle = {},
            onAddDetalleClick = {},
            onGuardarClick = {}
        )
    }
}
