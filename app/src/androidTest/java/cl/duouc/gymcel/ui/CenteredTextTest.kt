package cl.duouc.gymcel.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.gymcel.presentacion.ui.components.CenteredText
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CenteredTextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun centeredText_displaysProvidedText() {

        val text = "Cargando entrenamiento..."

        composeTestRule.setContent {
            CenteredText(text = text)
        }

        composeTestRule
            .onNodeWithText(text)
            .assertIsDisplayed()
    }
}