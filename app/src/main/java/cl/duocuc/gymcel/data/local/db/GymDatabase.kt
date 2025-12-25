package cl.duocuc.gymcel.data.local.db
import androidx.room.Database
import androidx.room.RoomDatabase
import cl.duocuc.gymcel.data.local.dao.ItemRutinaDao
import cl.duocuc.gymcel.data.local.dao.ItemTreinoDao
import cl.duocuc.gymcel.data.local.dao.MaestroRutinaDao
import cl.duocuc.gymcel.data.local.dao.MaestroTreinoDao
import cl.duocuc.gymcel.data.local.dao.RutinaDao
import cl.duocuc.gymcel.data.local.dao.TreinoDao
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import cl.duocuc.gymcel.data.local.entities.ItemRutinaEntity
import cl.duocuc.gymcel.data.local.entities.ItemTreinoEntity
import cl.duocuc.gymcel.data.local.entities.TreinoEntity

@Database(
    entities = [
        RutinaEntity::class,
        ItemRutinaEntity::class,
        TreinoEntity::class,
        ItemTreinoEntity::class
    ],
    version = 6
)
abstract class GymDatabase : RoomDatabase() {


    abstract fun rutinaDao(): RutinaDao

    abstract fun itemRutinaDao(): ItemRutinaDao

    abstract fun treinoDao(): TreinoDao

    abstract fun itemTreinoDao(): ItemTreinoDao

    abstract fun maestroRutinaDao(): MaestroRutinaDao

    abstract fun maestroTreinoDao(): MaestroTreinoDao

}
