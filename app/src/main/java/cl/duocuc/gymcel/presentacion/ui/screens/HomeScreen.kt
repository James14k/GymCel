package cl.duocuc.gymcel.presentacion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duocuc.gymcel.AppRoutes
import cl.duocuc.gymcel.presentacion.ui.components.BottomNavBar
import cl.duocuc.gymcel.presentacion.ui.components.TopNavBar
import cl.duocuc.gymcel.presentacion.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(),
) {

    val treinoPendiente by viewModel.treinoPendiente.collectAsState()
    val totalTreinos by viewModel.totalTreinos.collectAsState()

    Scaffold(
        topBar = { TopNavBar(title = "Inicio") },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))


            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "¡Hola, Atleta!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Tu Progreso Diario 📈",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))



            if (treinoPendiente != null) {
                Text(
                    text = "Sesión Pendiente",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            treinoPendiente!!.rutinaNombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("${treinoPendiente!!.dia} | ${treinoPendiente!!.ejerciciosPendientes} Ejercicios pendientes")
                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            // treino id si esta en el pendiente, o 0L por si acaso
                            navController.navigate(AppRoutes.DETALLE_TREINO(treinoPendiente?.treinoId ?: 0L))
                        }) {
                            Text("Continuar Treino")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            } else {

                Text(
                    text = "¡Todo al día! Comienza una nueva rutina para registrar tu progreso.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }



            Text(
                text = "Tus Números",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                StatCard(
                    title = "Treinos Completados",
                    value = totalTreinos.toString(),
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                // StatCard 2: Sigue simulando el PR
                StatCard(
                    title = "Último PR",
                    value = "Squat (140kg)",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}




@Composable
fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}