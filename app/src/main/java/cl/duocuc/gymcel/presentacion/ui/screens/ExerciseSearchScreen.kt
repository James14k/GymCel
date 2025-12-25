package cl.duocuc.gymcel.presentacion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cl.duocuc.gymcel.presentacion.ui.components.EjercicioListItem
import cl.duocuc.gymcel.presentacion.ui.components.TopNavBar
import cl.duocuc.gymcel.presentacion.viewmodel.ExerciseSearchEvent
import cl.duocuc.gymcel.presentacion.viewmodel.ExerciseSearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseSearchScreen(
    viewModel: ExerciseSearchViewModel,
    onExerciseSelected: (String) -> Unit,
    onOpenDetail: (String) -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val state = viewModel.state
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopNavBar(
                showBackButton = onBackClick != null,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 🔍 SEARCH BAR
            SearchBar(
                query = state.query,
                onQueryChange = { viewModel.onEvent(ExerciseSearchEvent.OnQueryChanged(it)) },
                onSearch = {
                    focusManager.clearFocus()
                    keyboard?.hide()
                },
                active = false,
                onActiveChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar ejercicio...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                shape = RoundedCornerShape(16.dp)
            ) {}

            // LOADING
            if (state.isLoading) {
                Text(
                    text = "...",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // ERROR
            state.error?.let { errorMsg ->
                Text(
                    text = errorMsg,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // LISTA
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.results) { ejercicio ->
                    Column {
                        EjercicioListItem(
                            ejercicio = ejercicio,
                            onItemClick = { onExerciseSelected(ejercicio.id) },
                            onIconClick = { onOpenDetail(ejercicio.id) }
                        )

                        Divider(
                            color = MaterialTheme.colorScheme.outline,
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

        }
    }
}

