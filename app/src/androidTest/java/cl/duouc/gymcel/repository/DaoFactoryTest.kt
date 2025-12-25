package cl.duouc.gymcel.repository

import cl.duocuc.gymcel.data.local.dao.DaoFactoryImpl
import cl.duocuc.gymcel.data.local.dao.RutinaDao
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock

class DaoFactoryTest {

    @Test
    fun daoFactory_returns_correct_dao() {
        val fakeDao = mock(RutinaDao::class.java)

        val factory = DaoFactoryImpl(
            mapOf(RutinaEntity::class.java to { fakeDao })
        )

        val dao = factory.create<RutinaDao>(RutinaEntity::class.java)
        assertTrue(dao is RutinaDao)
    }
}
