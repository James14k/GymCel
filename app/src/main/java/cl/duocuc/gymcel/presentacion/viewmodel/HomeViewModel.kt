package cl.duocuc.gymcel.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duocuc.gymcel.data.FactoryProvider
import cl.duocuc.gymcel.data.local.db.GymDatabase
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import cl.duocuc.gymcel.data.local.entities.TreinoEntity
import cl.duocuc.gymcel.domain.data.RepositoryFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TreinoPendienteUI(
    val treinoId: Long,
    val rutinaNombre: String,
    val dia: String,
    val ejerciciosPendientes: Int // Simulado, se puede hacer real con ItemRutina y ItemTreino
)

class HomeViewModel(db: GymDatabase) : ViewModel() {

    private val registry by lazy { FactoryProvider.registry(db) }
    private val repositoryFactory: RepositoryFactory by lazy { FactoryProvider.repositoryFactory(registry) }

    private val treinoRepository by lazy { repositoryFactory.create(TreinoEntity::class.java) }
    private val rutinaRepository by lazy { repositoryFactory.create(RutinaEntity::class.java) }


    private val _treinoPendiente = MutableStateFlow<TreinoPendienteUI?>(null)
    val treinoPendiente: StateFlow<TreinoPendienteUI?> = _treinoPendiente.asStateFlow()


    private val _totalTreinos = MutableStateFlow(0)
    val totalTreinos: StateFlow<Int> = _totalTreinos.asStateFlow()

    init {
        cargarTreinoPendiente()
        cargarTotalTreinosCompletados()
    }

    private fun cargarTreinoPendiente() {
        viewModelScope.launch {

            val ultimoPendiente = treinoRepository.getAll()
                .filter { !it.done }
                .maxByOrNull { it.timestamp }

            if (ultimoPendiente != null) {
                val rutina = rutinaRepository.getById(ultimoPendiente.rutina_id)

                _treinoPendiente.value = TreinoPendienteUI(
                    treinoId = ultimoPendiente.id,
                    rutinaNombre = rutina?.name ?: "Rutina Desconocida",
                    dia = rutina?.dia ?: "Día Desconocido",
                    ejerciciosPendientes = 4 // Simulación
                )
            } else {
                _treinoPendiente.value = null
            }
        }
    }


    private fun cargarTotalTreinosCompletados() {
        viewModelScope.launch {
            val completedCount = treinoRepository.getAll().count { it.done }

            _totalTreinos.value = completedCount
        }
    }
}