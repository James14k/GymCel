package cl.duocuc.gymcel.data.local.master

import cl.duocuc.gymcel.data.local.dao.ItemRutinaDao
import cl.duocuc.gymcel.data.local.dao.MaestroRutinaDao
import cl.duocuc.gymcel.data.local.dao.RutinaDao
import cl.duocuc.gymcel.data.local.entities.relations.MaestroRutina
import cl.duocuc.gymcel.domain.data.GymcelControlEntidadMaestra

class ControlMaestroRutina(
    private val maestroDao: MaestroRutinaDao,
    private val rutinaDao: RutinaDao,
    private val itemRutinaDao: ItemRutinaDao
): GymcelControlEntidadMaestra<MaestroRutina> {

    override suspend fun load(id: Long): MaestroRutina? {
        return maestroDao.getById(id)
    }

    override suspend fun loadAll(): List<MaestroRutina> {
        return maestroDao.getAll()
    }

    override suspend fun save(aggregate: MaestroRutina): Long {
        val rutina = aggregate.rutina

        val rutinaId = if (rutina.id == 0L) {
            rutinaDao.insert(rutina)
        } else {
            rutinaDao.update(rutina)
            rutina.id
        }

        aggregate.entries.forEach { item ->
            if (item.id != 0L) {
                itemRutinaDao.delete(item)
            }
        }

        val itemsPersistidos = aggregate.entries.map {
            it.copy(rutina_id = rutinaId)
        }

        itemRutinaDao.insertAll(itemsPersistidos)

        return rutinaId
    }

    override suspend fun saveAll(aggregates: List<MaestroRutina>): List<Long> {
        val ids = mutableListOf<Long>()
        for (aggregate in aggregates) {
            ids += save(aggregate)
        }
        return ids
    }

    override suspend fun delete(aggregate: MaestroRutina) {
        val rutina = aggregate.rutina

        aggregate.entries.forEach {
            itemRutinaDao.delete(it)
        }

        rutinaDao.delete(rutina)
    }
}