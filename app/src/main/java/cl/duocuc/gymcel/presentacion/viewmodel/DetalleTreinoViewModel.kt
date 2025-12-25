package cl.duocuc.gymcel.presentacion.viewmodel

import cl.duocuc.gymcel.data.service.ItemTreinoDataService
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duocuc.gymcel.data.FactoryProvider
import cl.duocuc.gymcel.data.api.exercise.ExerciseDbApiService
import cl.duocuc.gymcel.data.local.dao.ItemTreinoDao
import cl.duocuc.gymcel.data.local.db.GymDatabase
import cl.duocuc.gymcel.data.local.entities.ItemRutinaEntity
import cl.duocuc.gymcel.data.local.entities.ItemTreinoEntity
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import cl.duocuc.gymcel.data.local.entities.TreinoEntity
import cl.duocuc.gymcel.data.mapper.toDomain
import cl.duocuc.gymcel.domain.data.RepositoryFactory
import cl.duocuc.gymcel.domain.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant

class DetalleTreinoViewModel(
    db :GymDatabase,
    private val api: ExerciseDbApiService
) : ViewModel() {
    private val _uiState = MutableStateFlow<ExerciseDetailUiState>(ExerciseDetailUiState.Loading)
    private val registry by lazy {
        FactoryProvider.registry(db)
    }
    private val repositoryFactory: RepositoryFactory by lazy {
        FactoryProvider.repositoryFactory(registry)
    }

    private val rutinaRepository by lazy {
        repositoryFactory.create(RutinaEntity::class.java)
    }

    private val itemRutinaRepository by lazy {
        repositoryFactory.create(ItemRutinaEntity::class.java)
    }

    private val treinoRepository by lazy {
        repositoryFactory.create(TreinoEntity::class.java)
    }

    private val itemTreinoRepository by lazy {
        repositoryFactory.create(ItemTreinoEntity::class.java)
    }
    private val itemTreinoDao by lazy {
        FactoryProvider.daoFactory(registry).create(ItemTreinoEntity::class.java) as ItemTreinoDao
    }
    private val itemTreinoService by lazy {
        ItemTreinoDataService(
            db = db,
            dao = itemTreinoDao
        )
    }

    // ---------------- STATES ----------------
    private val _rutina = MutableStateFlow<Rutina?>(null)
    val rutina: StateFlow<Rutina?> = _rutina.asStateFlow()

    private val _seriesUI = MutableStateFlow<Map<Long, List<SerieUI>>>(emptyMap())
    val seriesUI: StateFlow<Map<Long, List<SerieUI>>> = _seriesUI.asStateFlow()

    private val _editable = MutableStateFlow(true)
    val editable: StateFlow<Boolean> = _editable.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    val popupUltimoTreino = MutableStateFlow<List<Pair<Double, Int>>?>(null)
    val popupEjercicio = MutableStateFlow<String?>(null)
    private var treinoIdActual: Long? = null
    private val _detallesRutina = MutableStateFlow<List<ItemRutinaEntity>>(emptyList())
    val detallesRutina: StateFlow<List<ItemRutinaEntity>> = _detallesRutina.asStateFlow()
    private val _nombresEjercicios = MutableStateFlow<Map<String, String>>(emptyMap())
    val nombresEjercicios: StateFlow<Map<String, String>> = _nombresEjercicios.asStateFlow()


    fun cargarTreino(treinoId: Long) {
        _loading.value = true

        viewModelScope.launch {
            try {
                val treinoActivo = treinoRepository.getById(treinoId) ?: return@launch

                val rutinaEntity = rutinaRepository.getById(treinoActivo.rutina_id) ?: return@launch
                _rutina.value = rutinaEntity.toDomain()

                val detallesBase = cargarDetallesRutina(rutinaEntity.id)
                _detallesRutina.value = detallesBase

                treinoIdActual = treinoId
                _editable.value = !treinoActivo.done

                _seriesUI.value = cargarSeriesUI(detallesBase, treinoActivo)

            } finally {
                _loading.value = false
            }
        }
    }


    suspend fun obtenerUltimoTreino(detalle: ItemRutinaEntity): List<Pair<Double, Int>> {

        val ultimo = itemTreinoService.getUltimoTreinoPorEjercicio(
            exerciseId = detalle.exercise_externalid,
            rutinaId = detalle.rutina_id
        )
        return ultimo.map { it.effective_load to it.effective_reps }
    }


    fun mostrarUltimoTreino(detalle: ItemRutinaEntity) {
        viewModelScope.launch {
            val ultimo = obtenerUltimoTreino(detalle)
            Log.d("RutinaDetalle", "Datos para el popup: $ultimo")
            popupUltimoTreino.value = ultimo
            popupEjercicio.value = detalle.exercise_externalid
        }
    }




    fun cerrarPopupUltimoTreino() {
        popupUltimoTreino.value = null
    }

    fun actualizarCarga(detalleId: Long, serieIndex: Int, nuevaCarga: Double) {
        if (!_editable.value) return
        val copia = _seriesUI.value.toMutableMap()
        val lista = copia[detalleId]?.toMutableList() ?: return
        val serie = lista[serieIndex]
        lista[serieIndex] = serie.copy(carga = nuevaCarga)
        copia[detalleId] = lista
        _seriesUI.value = copia

        guardarSerie(detalleId, serieIndex, lista[serieIndex])
    }

    fun actualizarReps(detalleId: Long, serieIndex: Int, nuevasReps: Int) {
        if (!_editable.value) return
        val copia = _seriesUI.value.toMutableMap()
        val lista = copia[detalleId]?.toMutableList() ?: return
        val serie = lista[serieIndex]
        lista[serieIndex] = serie.copy(reps = nuevasReps)
        copia[detalleId] = lista
        _seriesUI.value = copia

        guardarSerie(detalleId, serieIndex, lista[serieIndex])
    }

    fun actualizarUnidad(detalleId: Long, serieIndex: Int, nuevaUnidad: UnidadPeso) {
        if (!_editable.value) return

        val copia = _seriesUI.value.toMutableMap()
        val lista = copia[detalleId]?.toMutableList() ?: return
        val serie = lista[serieIndex]

        val converted = nuevaUnidad.convert(
            value = serie.carga,
            sourceUnit = serie.unidad
        )

        lista[serieIndex] = serie.copy(
            unidad = nuevaUnidad,
            carga = converted
        )

        copia[detalleId] = lista
        _seriesUI.value = copia

        guardarSerie(detalleId, serieIndex, lista[serieIndex])
    }


    private fun guardarSerie(detalleId: Long, serieIndex: Int, serieUI: SerieUI) {
        val treinoId = treinoIdActual ?: return

        viewModelScope.launch {
            val detalleEntity = itemRutinaRepository.getById(detalleId) ?: return@launch

            val itemsTreino = itemTreinoRepository.getAll()
                .filter { it.treino_id == treinoId && it.exercise_externalid == detalleEntity.exercise_externalid }
                .sortedBy { it.id }

            val itemExistente = itemsTreino.getOrNull(serieIndex)


            val entidadAGuardar = ItemTreinoEntity(
                id = itemExistente?.id ?: 0,
                treino_id = treinoId,
                exercise_externalid = detalleEntity.exercise_externalid,
                effective_reps = serieUI.reps,
                effective_load = serieUI.carga,
                load_unit = serieUI.unidad.symbol,
                rir = null,
                rest_nanos = 0L
            )


            if (itemExistente != null) {

                itemTreinoRepository.update(entidadAGuardar)

            } else {

                itemTreinoRepository.save(entidadAGuardar)
            }
        }
    }

    fun guardarTreino() {
        val treinoId = treinoIdActual ?: return

        viewModelScope.launch {
            // Asegurarse de que el Treino se marca como COMPLETADO
            val treino = treinoRepository.getById(treinoId) ?: return@launch

            treinoRepository.update(
                treino.copy(
                    timestamp = Instant.now().epochSecond,
                    done = true, // 🚀 Único lugar donde se establece a TRUE
                    // notas = ... (si se agregan notas a la UI)
                )
            )

            _editable.value = false
        }
    }

    // ---------------- INTERNALS ----------------

    private suspend fun cargarDetallesRutina(rutinaId: Long) =
        itemRutinaRepository.getAll().filter { it.rutina_id == rutinaId }



    private suspend fun cargarOCrearTreino(rutinaId: Long): TreinoEntity {

        val treinoPendiente = treinoRepository.getAll()
            .find { it.rutina_id == rutinaId && !it.done }

        if (treinoPendiente != null) {
            treinoIdActual = treinoPendiente.id
            return treinoPendiente
        }

        val nuevoId = treinoRepository.save(
            TreinoEntity(
                id = 0,
                rutina_id = rutinaId,
                timestamp = Instant.now().epochSecond,
                done = false,
                notas = null
            )
        )

        treinoIdActual = nuevoId
        return treinoRepository.getById(nuevoId)!!
    }

    private suspend fun cargarSeriesUI(
        detalles: List<ItemRutinaEntity>,
        treino: TreinoEntity
    ): Map<Long, List<SerieUI>> {

        val itemsTreino = itemTreinoRepository.getAll().filter { it.treino_id == treino.id }

        val editableGlobal = !treino.done
        val resultado = mutableMapOf<Long, List<SerieUI>>()

        detalles.forEach { detalle ->

            val seriesGuardadas = itemsTreino
                .filter { it.exercise_externalid == detalle.exercise_externalid }
                .sortedBy { it.id }

            val listaUI = List(detalle.sets_amount) { index ->

                val itemGuardado = seriesGuardadas.getOrNull(index)

                val cargaInicial = itemGuardado?.effective_load ?: 0.0
                val repsInicial = itemGuardado?.effective_reps ?: 0
                val unidadInicial = UnidadPeso.values().find { u -> u.symbol == itemGuardado?.load_unit }
                    ?: UnidadPeso.KILOGRAM

                SerieUI(
                    numero = index + 1,
                    carga = cargaInicial,
                    reps = repsInicial,
                    unidad = unidadInicial,
                    meta = construirMeta(detalle),
                    editable = editableGlobal
                )
            }

            resultado[detalle.id] = listaUI
        }

        return resultado
    }


    private fun construirMeta(detalle: ItemRutinaEntity): String? = when {
        detalle.reps_goal != null ->
            "${detalle.reps_goal} reps"

        detalle.reps_range_min != null && detalle.reps_range_max != null ->
            "${detalle.reps_range_min}-${detalle.reps_range_max} reps"

        detalle.reps_range_max != null ->
            "${detalle.reps_range_max} reps"

        else -> null
    }
    fun cargarNombreEjercicio(externalId: String) {
        viewModelScope.launch {
            if (_nombresEjercicios.value.containsKey(externalId)) return@launch

            val ejercicio = obtenerNombreEjercicio(externalId)
            if (ejercicio != null) {
                val copia = _nombresEjercicios.value.toMutableMap()
                copia[externalId] = ejercicio.nombre
                _nombresEjercicios.value = copia
            }
        }
    }

    private suspend fun obtenerNombreEjercicio(externalId: String): Ejercicio? {
        var body: Ejercicio? = null
        try {
            val response = api.getExerciseById(externalId)

            if (response.isSuccessful) {
                 body = response.body()?.data?.toDomain()

                if (body != null) {
                    _uiState.value = ExerciseDetailUiState.Success(body)
                    return body
                } else {
                    _uiState.value = ExerciseDetailUiState.Error("Respuesta vacía")
                }
            } else {
                _uiState.value = ExerciseDetailUiState.Error(
                    "Error HTTP: ${response.code()}"
                )
            }

        } catch (ex: Exception) {
            _uiState.value = ExerciseDetailUiState.Error(ex.message ?: "Error desconocido")

        }
        return body
    }

}



