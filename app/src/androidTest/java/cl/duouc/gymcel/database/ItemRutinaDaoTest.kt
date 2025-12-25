package cl.duouc.gymcel.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.gymcel.data.local.db.GymDatabase
import cl.duocuc.gymcel.data.local.entities.ItemRutinaEntity
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemRutinaDaoTest {

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
    fun insertItemRutina_fk_ok() = runTest {
        val rutinaId = db.rutinaDao()
            .insert(RutinaEntity(name = "Full", desc = null))

        val itemId = db.itemRutinaDao().insert(
            ItemRutinaEntity(
                rutina_id = rutinaId,
                exercise_externalid = "squat",
                sets_amount = 4,
                reps_goal = 10,
                reps_range_min = null,
                reps_range_max = null,
                variation = "STRAIGHT"
            )
        )

        assertTrue(itemId > 0)
    }

    @Test
    fun getAllItemRutina_ok() = runTest {
        val rutinaId = db.rutinaDao()
            .insert(RutinaEntity(name = "Upper", desc = null))

        db.itemRutinaDao().insert(
            ItemRutinaEntity(
                rutina_id = rutinaId,
                exercise_externalid = "bench",
                sets_amount = 3,
                reps_goal = 8,
                reps_range_min = null,
                reps_range_max = null,
                variation = "STRAIGHT"
            )
        )

        assertEquals(1, db.itemRutinaDao().getAll().size)
    }
}
