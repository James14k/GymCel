package cl.duouc.gymcel.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.gymcel.data.local.db.GymDatabase
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import cl.duocuc.gymcel.data.local.entities.TreinoEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TreinoDaoTest {

    private lateinit var db: GymDatabase

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GymDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun insertTreino_ok() = runTest {
        val rutinaId = db.rutinaDao()
            .insert(RutinaEntity(name = "Cardio", desc = null))

        val id = db.treinoDao().insert(
            TreinoEntity(rutina_id = rutinaId, notas = null)
        )

        assertTrue(id > 0)
    }

    @Test
    fun countTreino_ok() = runTest {
        val rutinaId = db.rutinaDao()
            .insert(RutinaEntity(name = "Test", desc = null))

        db.treinoDao().insert(TreinoEntity(rutina_id = rutinaId, notas = null))
        assertEquals(1, db.treinoDao().count())
    }
}
