package cl.duocuc.gymcel.presentacion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duocuc.gymcel.domain.model.SerieUI
import cl.duocuc.gymcel.domain.model.UnidadPeso

@Composable
fun SeriesTable(
    detalleId: Long,
    series: List<SerieUI>,
    editable: Boolean,
    onCarga: (Long, Int, Double) -> Unit,
    onReps: (Long, Int, Int) -> Unit,
    onUnidad: (Long, Int, UnidadPeso) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Header de columnas - MÁS COMPACTO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.width(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    "#",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                "Carga",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Box(modifier = Modifier.width(60.dp), contentAlignment = Alignment.Center) {
                Text(
                    "Unidad",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.Center) {
                Text(
                    "Reps",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            if (series.firstOrNull()?.meta != null) {
                Box(modifier = Modifier.width(85.dp), contentAlignment = Alignment.Center) {
                }
            }
        }

        series.forEachIndexed { index, serie ->
            SerieRow(
                serie = serie,
                index = index,
                detalleId = detalleId,
                editable = editable,
                onCarga = onCarga,
                onReps = onReps,
                onUnidad = onUnidad
            )
        }
    }
}

@Composable
private fun SerieRow(
    serie: SerieUI,
    index: Int,
    detalleId: Long,
    editable: Boolean,
    onCarga: (Long, Int, Double) -> Unit,
    onReps: (Long, Int, Int) -> Unit,
    onUnidad: (Long, Int, UnidadPeso) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = if (serie.editable && editable)
            MaterialTheme.colorScheme.surface
        else
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        tonalElevation = if (serie.editable && editable) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Número de serie - MÁS PEQUEÑO
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${serie.numero}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Carga - OCUPA TODO EL ESPACIO DISPONIBLE
            OutlinedTextField(
                value = if (serie.carga == 0.0) "" else serie.carga.toString(),
                onValueChange = {
                    if (it.isEmpty()) onCarga(detalleId, index, 0.0)
                    else it.toDoubleOrNull()?.let { v -> onCarga(detalleId, index, v) }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = serie.editable && editable,
                placeholder = {
                    Text(
                        "0.0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            )

            // Unidad - ANCHO FIJO
            UnidadPicker(
                selected = serie.unidad,
                enabled = serie.editable && editable,
                onSelect = { onUnidad(detalleId, index, it) }
            )

            // Reps - ANCHO FIJO
            OutlinedTextField(
                value = if (serie.reps == 0) "" else serie.reps.toString(),
                onValueChange = {
                    if (it.isEmpty()) onReps(detalleId, index, 0)
                    else it.toIntOrNull()?.let { v -> onReps(detalleId, index, v) }
                },
                modifier = Modifier
                    .width(80.dp)
                    .height(48.dp),
                enabled = serie.editable && editable,
                placeholder = {
                    Text(
                        "0",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            )

            // Meta - ANCHO FIJO
            if (serie.meta != null) {
                Box(
                    modifier = Modifier
                        .width(85.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = serie.meta,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }
    }
}