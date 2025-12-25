package cl.duocuc.gymcel.presentacion.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duocuc.gymcel.AppRoutes
import cl.duocuc.gymcel.presentacion.ui.components.BottomNavBar
import cl.duocuc.gymcel.presentacion.ui.components.TopNavBar
import cl.duocuc.gymcel.presentacion.ui.components.TreinoCard
import cl.duocuc.gymcel.presentacion.viewmodel.WorkoutLogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutLogScreen(
    navController: NavController,
    viewModel: WorkoutLogViewModel = viewModel()
) {
    // 1. Recolectar la lista de Treinos (usando el nuevo StateFlow)
    val treinosLog by viewModel.treinosUI.collectAsState()
    val context = LocalContext.current
    val exportCSV by viewModel.exportCSVEvent.collectAsState()

    val saveLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { output ->
                output.write(exportCSV?.toByteArray() ?: ByteArray(0))
            }
            viewModel.limpiarEventoExport()
        }
    }

    LaunchedEffect(exportCSV) {
        if (!exportCSV.isNullOrEmpty()) {
            saveLauncher.launch("historial_treinos.csv")
        }
    }
    Scaffold(
        topBar = { TopNavBar() },
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    "Historial de Treinos",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 80.dp) // espacio para FABs
                ) {
                    items(treinosLog, key = { it.treinoId }) { treino ->
                        TreinoCard(
                            treino = treino,
                            onClick = {
                                navController.navigate(AppRoutes.DETALLE_TREINO(treino.treinoId))
                            }
                        )
                    }
                }

                if (treinosLog.isEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        "Aún no has completado ningún treino.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // --- FAB derecha (nuevo treino) ---
            FloatingActionButton(
                onClick = { navController.navigate(AppRoutes.SELECTOR_RUTINA()) },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Iniciar nuevo treino")
            }

            // --- FAB izquierda (exportar CSV) ---
            FloatingActionButton(
                onClick = { viewModel.exportarCSV() },
                containerColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = "Exportar CSV")
            }
        }
    }

}
