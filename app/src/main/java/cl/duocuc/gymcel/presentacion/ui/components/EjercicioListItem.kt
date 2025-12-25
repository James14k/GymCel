package cl.duocuc.gymcel.presentacion.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duocuc.gymcel.domain.model.DetalleEjercicio
import cl.duocuc.gymcel.domain.model.Ejercicio

@Composable
fun EjercicioListItem(
    ejercicio: Ejercicio,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    onIconClick: () -> Unit,
    icon: ImageVector = Icons.Default.Help
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(modifier = Modifier.weight(1f)) {

            // Nombre
            Text(
                text = ejercicio.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            ejercicio.detalle?.let { dt ->

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Músculos
                    Text(
                        text = dt.musculosTrabajados.joinToString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )

                }
            }
        }

        // Icono parametrizable (acción independiente)
        Icon(
            imageVector = icon,
            contentDescription = "Acción",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(start = 12.dp)
                .clickable(onClick = onIconClick)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEjercicioListItem() {
    EjercicioListItem(
        ejercicio = Ejercicio(
            id = "2",
            nombre = "Press banca",
            detalle = DetalleEjercicio(
                musculosTrabajados = listOf("Pectoral", "Tríceps"),
                equipamiento = listOf("Barra"),
                instrucciones = emptyList()
            )
        ),
        onItemClick = {},
        onIconClick = {}
    )
}
