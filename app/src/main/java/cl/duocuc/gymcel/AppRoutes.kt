package cl.duocuc.gymcel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import cl.duocuc.gymcel.core.navigation.route;
import cl.duocuc.gymcel.domain.usecase.useApi
import cl.duocuc.gymcel.presentacion.ui.components.BottomNavItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object AppRoutes {

    val HOME = route {
        text("home")
    }

    val WORKOUT_LOG = route {
        text("wklog")
    }

    val SELECTOR_RUTINA = route {
        text("selrutina")
    }

    @Volatile
    private var navItems: List<BottomNavItem>? = null

    fun getBottomNavItems(): List<BottomNavItem> {
        return navItems ?: synchronized(this) {
            navItems ?: listOf(
                BottomNavItem(
                    title = "Inicio",
                    route = HOME(),
                    icon = Icons.Default.Home
                ),
                BottomNavItem(
                    title = "Log",
                    route = WORKOUT_LOG(),
                    icon = Icons.Default.Book
                ),
                BottomNavItem(
                    title = "Crear Rutina",
                    route = RUTINA_FORM(),
                    icon = Icons.Default.FitnessCenter
                )
            ).also{ navItems = it }
        }
    }

    fun getTopNavbarActions(
        scope: CoroutineScope
    ): Map<String, () -> Unit> {
        return mapOf(
            "API TEST" to {
                scope.launch {
                    useApi(AppConstants.Api.jsonPlaceholder())
                }
            }
        )
    }



    val DETALLE_TREINO = route {
        text("treino")
        param("id")
    }

    val RUTINA_FORM = route {
        text("rutina")
    }

    val BUSCAR_EJERCICIO = route {
        text("search")
        text("exercise")
    }

    val DETALLE_EJERCICIO = route {
        text("exercise")
        param("id")
    }


}