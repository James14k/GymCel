package cl.duouc.gymcel.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.gymcel.domain.model.DetalleEjercicio
import cl.duocuc.gymcel.domain.model.Ejercicio
import cl.duocuc.gymcel.presentacion.ui.components.EjercicioListItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EjercicioListItemTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun ejercicioListItem_displaysExerciseName() {

        val ejercicio = Ejercicio(
            id = "1",
            nombre = "Press banca",
            detalle = DetalleEjercicio(
                musculosTrabajados = listOf("Pecho"),
                equipamiento = emptyList(),
                instrucciones = emptyList()
            )
        )

        composeTestRule.setContent {
            EjercicioListItem(
                ejercicio = ejercicio,
                onItemClick = {},
                onIconClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Press banca")
            .assertIsDisplayed()
    }
}