package cl.duocuc.gymcel.presentacion.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cl.duocuc.gymcel.domain.model.DetalleRutina
import cl.duocuc.gymcel.domain.model.TipoSerie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRutinaFormCard(
    ejercicioNombre: String,
    ejercicioId: String,
    ordenInferido: Int,
    initial: DetalleRutina? = null,
    onValueChange: (DetalleRutina) -> Unit
) {
    var seriesText by remember { mutableStateOf((initial?.series ?: 3).toString()) }
    var repsObjetivoText by remember { mutableStateOf((initial?.objetivoReps ?: 10).toString()) }

    var isRange by remember { mutableStateOf(initial?.isRange() ?: false) }
    var rangoMinText by remember { mutableStateOf((initial?.rangoReps?.first ?: 8).toString()) }
    var rangoMaxText by remember { mutableStateOf((initial?.rangoReps?.last ?: 12).toString()) }

    var tipoSerie by remember { mutableStateOf(initial?.tipoSerie ?: TipoSerie.STRAIGHT) }
    var tipoExpand by remember { mutableStateOf(false) }

    val focusSeries = remember { FocusRequester() }
    val focusRangoMin = remember { FocusRequester() }
    val focusRangoMax = remember { FocusRequester() }
    val focusReps = remember { FocusRequester() }

    LaunchedEffect(
        seriesText,
        repsObjetivoText,
        isRange,
        rangoMinText,
        rangoMaxText,
        tipoSerie
    ) {
        val series = seriesText.toIntOrNull() ?: 0
        val repsObjetivo = repsObjetivoText.toIntOrNull()
        val rangoMin = rangoMinText.toIntOrNull()
        val rangoMax = rangoMaxText.toIntOrNull()

        val detalle = DetalleRutina(
            id = initial?.id ?: 0,
            ejercicioId = ejercicioId,
            orden = ordenInferido,
            series = series,
            objetivoReps = if (!isRange) repsObjetivo else null,
            rangoReps = if (isRange && rangoMin != null && rangoMax != null) rangoMin..rangoMax else null,
            tipoSerie = tipoSerie
        )

        onValueChange(detalle)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = ejercicioNombre,
                style = MaterialTheme.typography.titleMedium
            )

            Divider()

            OutlinedTextField(
                value = seriesText,
                onValueChange = { input ->
                    if (input.isEmpty() || input.all { it.isDigit() }) {
                        seriesText = input
                    }
                },
                label = { Text("Series") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (isRange) focusRangoMin.requestFocus()
                        else focusReps.requestFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusSeries)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Usar rango de repeticiones")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isRange,
                    onCheckedChange = { isRange = it }
                )
            }

            if (isRange) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(
                        value = rangoMinText,
                        onValueChange = { input ->
                            if (input.isEmpty() || input.all { it.isDigit() }) {
                                rangoMinText = input
                            }
                        },
                        label = { Text("Mínimo") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusRangoMax.requestFocus() }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRangoMin)
                    )

                    OutlinedTextField(
                        value = rangoMaxText,
                        onValueChange = { input ->
                            if (input.isEmpty() || input.all { it.isDigit() }) {
                                rangoMaxText = input
                            }
                        },
                        label = { Text("Máximo") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {}),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRangoMax)
                    )
                }
            } else {
                OutlinedTextField(
                    value = repsObjetivoText,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.all { it.isDigit() }) {
                            repsObjetivoText = input
                        }
                    },
                    label = { Text("Repeticiones objetivo") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {}),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusReps)
                )
            }

            ExposedDropdownMenuBox(
                expanded = tipoExpand,
                onExpandedChange = { tipoExpand = it }
            ) {
                OutlinedTextField(
                    value = tipoSerie.desc,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de serie") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = tipoExpand,
                    onDismissRequest = { tipoExpand = false }
                ) {
                    TipoSerie.entries.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo.desc) },
                            onClick = {
                                tipoSerie = tipo
                                tipoExpand = false
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetalleRutinaFormCardPreview() {
    MaterialTheme {
        DetalleRutinaFormCard(
            ejercicioNombre = "Press Banca",
            ejercicioId = "bench_press_001",
            ordenInferido = 1,
            initial = null
        ) { detalle ->
            println("Cambio → $detalle")
        }
    }
}

