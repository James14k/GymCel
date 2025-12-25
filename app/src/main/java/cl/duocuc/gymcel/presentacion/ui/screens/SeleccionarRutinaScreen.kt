package cl.duocuc.gymcel.presentacion.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duocuc.gymcel.presentacion.ui.components.BottomNavBar
import cl.duocuc.gymcel.presentacion.ui.components.TopNavBar
import cl.duocuc.gymcel.presentacion.viewmodel.SeleccionarRutinaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionarRutinaScreen(
    navController: NavController,
    viewModel: SeleccionarRutinaViewModel = viewModel(),
    onTreinoSeleccionado: (Long) -> Unit
) {

    val rutinas = viewModel.rutinas.collectAsState().value
    val rutinaSeleccionada = viewModel.rutinaSeleccionada.collectAsState().value
    val dropdownExpanded = viewModel.dropdownExpanded.collectAsState().value

    Scaffold(
        topBar = { TopNavBar() },
        bottomBar = {
            // Ahora ya no recibe navController, sólo UI
            BottomNavBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Seleccione Rutina",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(modifier = Modifier.fillMaxWidth()) {

                OutlinedTextField(
                    value = rutinaSeleccionada?.nombre ?: "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.abrirDropdown() },
                    label = { Text("Rutina") },
                    readOnly = true,
                    trailingIcon = {
                        IconButton(
                            modifier = Modifier.padding(end = 4.dp),
                            onClick = { viewModel.abrirDropdown() }
                        ) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Desplegar")
                        }
                    },
                    singleLine = true
                )

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { viewModel.cerrarDropdown() },
                    modifier = Modifier.fillMaxWidth(0.95f)
                ) {
                    rutinas.forEach { rutina ->
                        DropdownMenuItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            text = {
                                Text(
                                    rutina.nombre,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = {
                                viewModel.seleccionarRutina(rutina) { treinoId ->
                                    onTreinoSeleccionado(treinoId)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "(ordenado por rutina seteada para el día más cercano al actual)",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}