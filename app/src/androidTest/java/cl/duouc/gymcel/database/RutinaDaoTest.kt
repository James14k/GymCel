package cl.duouc.gymcel.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.gymcel.data.local.dao.RutinaDao
import cl.duocuc.gymcel.data.local.db.GymDatabase
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

@RunWith(AndroidJUnit4::class)
class RutinaDaoTest {

    private lateinit var db: GymDatabase
    private lateinit var dao: RutinaDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GymDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.rutinaDao()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun insertRutina_ok() = runTest {
        val id = dao.insert(RutinaEntity(name = "Push", desc = "RUTINA DE PUSH"))
        assertTrue(id > 0)
    }

    @Test
    fun getRutinaById_ok() = runTest {
        val id = dao.insert(RutinaEntity(name = "Pull", desc = "RUTINA DE PULL"))
        val rutina = dao.getById(id)
        assertEquals("Pull", rutina?.name)
    }

    @Test
    fun countRutinas_ok() = runTest {
        dao.insert(RutinaEntity(name = "A", desc = null))
        dao.insert(RutinaEntity(name = "B", desc = null))
        assertEquals(2, dao.count())
    }
}
