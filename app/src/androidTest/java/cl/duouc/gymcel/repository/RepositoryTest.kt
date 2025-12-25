package cl.duouc.gymcel.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cl.duocuc.gymcel.data.local.db.GymDatabase
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import cl.duocuc.gymcel.data.repository.GymcelRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    @Test
    fun repository_save_get_ok() = runTest {
        val db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            GymDatabase::class.java
        ).allowMainThreadQueries().build()

        val repo = GymcelRepository(db.rutinaDao())

        val id = repo.save(RutinaEntity(name = "Repo", desc = null))
        val rutina = repo.getById(id)

        assertEquals("Repo", rutina?.name)
        db.close()
    }
}
