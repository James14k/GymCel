package cl.duouc.gymcel.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.gymcel.presentacion.ui.components.TopNavBar
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TopNavBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun topNavBar_displaysTitle() {

        composeTestRule.setContent {
            TopNavBar(title = "Inicio")
        }

        composeTestRule
            .onNodeWithText("Inicio")
            .assertIsDisplayed()
    }
}