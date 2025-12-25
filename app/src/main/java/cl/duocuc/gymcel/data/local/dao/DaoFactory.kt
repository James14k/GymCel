package cl.duocuc.gymcel.data.local.dao

interface DaoFactory {

    fun <T : GymcelDao<*>> create(entityClass: Class<*>): T

}