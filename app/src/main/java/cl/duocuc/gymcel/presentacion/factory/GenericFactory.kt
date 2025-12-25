package cl.duocuc.gymcel.presentacion.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.duocuc.gymcel.data.api.exercise.ExerciseDbApiService
import cl.duocuc.gymcel.data.local.db.GymDatabase

class GenericViewModelFactory<TParam>(
    private val viewModelClass: Class<out ViewModel>,
    private val param: TParam,
    private val creator: (TParam) -> ViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            return creator(param) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

class DatabaseViewModelFactory<VM : ViewModel>(
    private val viewModelClass: Class<VM>,
    private val db: GymDatabase,
    private val creator: (GymDatabase) -> VM
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            return creator(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}


class ApiServiceViewModelFactory<VM : ViewModel>(
    private val viewModelClass: Class<VM>,
    private val api: ExerciseDbApiService,
    private val creator: (ExerciseDbApiService) -> VM
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(viewModelClass)) {
            return creator(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
