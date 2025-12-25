package cl.duocuc.gymcel.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duocuc.gymcel.data.FactoryProvider
import cl.duocuc.gymcel.data.local.db.GymDatabase
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import cl.duocuc.gymcel.data.local.entities.TreinoEntity
import cl.duocuc.gymcel.domain.model.Rutina
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class SeleccionarRutinaViewModel(
    db: GymDatabase
) : ViewModel() {

    private val registry = FactoryProvider.registry(db)

    private val rutinaRepository = FactoryProvider.repositoryFactory(registry)
        .create(RutinaEntity::class.java)
    private val treinoRepository = FactoryProvider.repositoryFactory(registry)
        .create(TreinoEntity::class.java)
    private val _rutinas = MutableStateFlow<List<Rutina>>(emptyList())
    val rutinas: StateFlow<List<Rutina>> = _rutinas.asStateFlow()

    private val _rutinaSeleccionada = MutableStateFlow<Rutina?>(null)
    val rutinaSeleccionada: StateFlow<Rutina?> = _rutinaSeleccionada.asStateFlow()

    private val _dropdownExpanded = MutableStateFlow(false)
    val dropdownExpanded: StateFlow<Boolean> = _dropdownExpanded.asStateFlow()

    init {
        cargarRutinas()
    }

    private fun cargarRutinas() {
        viewModelScope.launch {
            val entidades: List<RutinaEntity> = rutinaRepository.getAll()
            val modelos = entidades.mapNotNull { entidad ->
                // Convertir 'dia' de String? a DayOfWeek usando el nombre del día
                val dia = entidad.dia?.let { runCatching { DayOfWeek.valueOf(it) }.getOrNull() }

                Rutina(
                    id = entidad.id,
                    nombre = entidad.name,
                    descripcion = entidad.desc,
                    dia = dia,
                    detalles = emptyList()
                )
            }
            _rutinas.value = ordenarPorDiaMasCercano(modelos)
        }
    }


    fun seleccionarRutina(rutina: Rutina, onTreinoReady: (treinoId: Long) -> Unit) {
        _rutinaSeleccionada.value = rutina
        cerrarDropdown()

        viewModelScope.launch {
            // --- 1. Buscar Treino PENDIENTE ---
            val treinoPendiente = treinoRepository.getAll()
                .find { it.rutina_id == rutina.id && !it.done }

            val treinoId: Long = if (treinoPendiente != null) {
                // Caso A: Existe un treino pendiente
                treinoPendiente.id
            } else {
                // Caso B: No hay pendiente, crear uno nuevo

                // 🚀 CORRECCIÓN CLAVE: Usar save() y capturar el ID devuelto
                val nuevoTreinoId = treinoRepository.save(
                    TreinoEntity(
                        // id = 0 es crucial para autoGenerate
                        id = 0,
                        rutina_id = rutina.id,
                        // Usar Instant.now().epochSecond si el timestamp es Long/segundos
                        //vgonz: que tome el valor por defecto mejor...
                        //timestamp = java.time.Instant.now().epochSecond,
                        done = false,
                        notas = null
                    )
                )
                // Verificar si la base de datos devolvió un ID válido
                if (nuevoTreinoId == 0L) {
                    // Manejar error o log. Si save devuelve 0L, algo falló en Room/DAO.
                    return@launch
                }
                nuevoTreinoId
            }

            // --- 3. Navegar con el ID del Treino ---
            onTreinoReady(treinoId)
        }
    }

    fun abrirDropdown() {
        _dropdownExpanded.value = true
    }

    fun cerrarDropdown() {
        _dropdownExpanded.value = false
    }

    private fun ordenarPorDiaMasCercano(rutinas: List<Rutina>): List<Rutina> {
        val hoy = LocalDate.now().dayOfWeek.value
        return rutinas.sortedBy { rutina ->
            val index = rutina.dia?.value ?: 0
            (index - hoy + 7) % 7
        }
    }
}
