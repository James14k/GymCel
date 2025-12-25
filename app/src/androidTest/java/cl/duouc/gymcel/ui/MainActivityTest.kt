package cl.duouc.gymcel.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.gymcel.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun mainActivity_starts() {
        ActivityScenario.launch(MainActivity::class.java)
    }
}

