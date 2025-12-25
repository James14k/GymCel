package cl.duouc.gymcel.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.gymcel.domain.model.TreinoLogUI
import cl.duocuc.gymcel.presentacion.ui.components.TreinoCard

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.DayOfWeek
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class TreinoCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun treinoCard_showsCompletedStatus() {

        val treino = TreinoLogUI(
            treinoId = 1L,
            rutinaNombre = "Rutina Push",
            diaSemana = DayOfWeek.MONDAY,
            fecha = LocalDate.now(),
            isDone = true
        )

        composeTestRule.setContent {
            TreinoCard(
                treino = treino,
                onClick = {}
            )
        }

        composeTestRule
            .onNodeWithText("Completado")
            .assertIsDisplayed()
    }
}