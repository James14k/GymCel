package cl.duocuc.gymcel.presentacion

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cl.duocuc.gymcel.AppConstants
import cl.duocuc.gymcel.AppRoutes
import cl.duocuc.gymcel.core.navigation.composable
import cl.duocuc.gymcel.data.FactoryProvider
import cl.duocuc.gymcel.data.local.entities.ItemRutinaEntity
import cl.duocuc.gymcel.data.local.entities.RutinaEntity
import cl.duocuc.gymcel.data.local.entities.relations.MaestroRutina
import cl.duocuc.gymcel.data.local.master.ControlMaestroRutina
import cl.duocuc.gymcel.domain.model.Ejercicio
import cl.duocuc.gymcel.presentacion.factory.ApiServiceViewModelFactory
import cl.duocuc.gymcel.presentacion.factory.DatabaseViewModelFactory
import cl.duocuc.gymcel.presentacion.factory.GenericViewModelFactory
import cl.duocuc.gymcel.presentacion.ui.screens.DetalleTreinoScreen
import cl.duocuc.gymcel.presentacion.ui.screens.ExerciseDetailScreen
import cl.duocuc.gymcel.presentacion.ui.screens.ExerciseSearchScreen
import cl.duocuc.gymcel.presentacion.ui.screens.HomeScreen
import cl.duocuc.gymcel.presentacion.ui.screens.RutinaFormScreen
import cl.duocuc.gymcel.presentacion.ui.screens.SeleccionarRutinaScreen
import cl.duocuc.gymcel.presentacion.ui.screens.WorkoutLogScreen
import cl.duocuc.gymcel.presentacion.viewmodel.DetalleTreinoViewModel
import cl.duocuc.gymcel.presentacion.viewmodel.ExerciseDetailViewModel
import cl.duocuc.gymcel.presentacion.viewmodel.ExerciseSearchViewModel
import cl.duocuc.gymcel.presentacion.viewmodel.HomeViewModel
import cl.duocuc.gymcel.presentacion.viewmodel.RutinaFormViewModel
import cl.duocuc.gymcel.presentacion.viewmodel.SeleccionarRutinaViewModel
import cl.duocuc.gymcel.presentacion.viewmodel.WorkoutLogViewModel


@Composable
fun AppNavGraph(navController: NavHostController, context: Context) {
    val db = AppConstants.getDatabase(context)
    val exerciseDbApi = AppConstants.Api.exerciseDb()
    val jsonPlaceholderApi = AppConstants.Api.exerciseDb()
    val registry = FactoryProvider.registry(db)
    val repoFactory = FactoryProvider.repositoryFactory(registry)
    val daoFactory = FactoryProvider.daoFactory(registry)


    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME()
    ) {

        composable(AppRoutes.HOME()) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel(
                    factory = DatabaseViewModelFactory(
                        HomeViewModel::class.java,
                        db
                    ) { database -> HomeViewModel(database) }
                )
            )
        }

        composable(AppRoutes.SELECTOR_RUTINA()) {
            SeleccionarRutinaScreen(navController, viewModel(
                factory = DatabaseViewModelFactory(
                    SeleccionarRutinaViewModel::class.java,
                    db
                ) { database -> SeleccionarRutinaViewModel(database) }
            )) { id -> navController.navigate(AppRoutes.DETALLE_TREINO(id)) }
        }

        composable(AppRoutes.BUSCAR_EJERCICIO()) {
            ExerciseSearchScreen(
                viewModel = viewModel(
                    factory = ApiServiceViewModelFactory(
                        ExerciseSearchViewModel::class.java,
                        exerciseDbApi
                    ) { api -> ExerciseSearchViewModel(api) }
                ),

                onExerciseSelected = { id ->
                    // guardar resultado
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(AppConstants.StateKeys.EJERCICIO_SEL, id)

                    // volver
                    navController.popBackStack()
                },

                onOpenDetail = { id ->
                    navController.navigate(AppRoutes.DETALLE_EJERCICIO(id))
                },

                onBackClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.remove<String>(AppConstants.StateKeys.EJERCICIO_SEL)
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.RUTINA_FORM()) {
            RutinaFormScreen(
                viewModel = viewModel(
                    factory = GenericViewModelFactory(
                        RutinaFormViewModel::class.java,
                        ControlMaestroRutina(
                            maestroDao = daoFactory.create(MaestroRutina::class.java),
                            rutinaDao = daoFactory.create(RutinaEntity::class.java),
                            itemRutinaDao = daoFactory.create(ItemRutinaEntity::class.java)
                        ),
                    ) { param -> RutinaFormViewModel(aggStore = param) }
                ),
                navController = navController
            )
        }



        AppRoutes.DETALLE_EJERCICIO.composable(this) { params ->
            // lo dejamos nuleable porque la pantalla maneja el caso null internamente.
            val exerciseId = params.getOrNull(0)

            ExerciseDetailScreen(
                viewModel = viewModel(
                    factory = ApiServiceViewModelFactory(
                        ExerciseDetailViewModel::class.java,
                        exerciseDbApi
                    ) { api -> ExerciseDetailViewModel(api) }
                ),
                exerciseId = exerciseId,
                onSelect = { id -> println("Ejercicio seleccionado en detalle: $id") },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.WORKOUT_LOG()) {
            WorkoutLogScreen(
                navController,
                viewModel = viewModel(
                    factory = GenericViewModelFactory(
                        WorkoutLogViewModel::class.java,
                        repoFactory,
                    ) { param -> WorkoutLogViewModel(param) }
                    )
                )
        }

        AppRoutes.DETALLE_TREINO.composable(this) { params ->
            //FIXME: que pasa en la condicion que caiga en 0L ? como estamos manejando ese caso...
            val treinoId = params[0]?.toLongOrNull() ?: 0L

            DetalleTreinoScreen(
                navController = navController,
                treinoId = treinoId,
                viewModel = viewModel(
                    factory = DatabaseViewModelFactory(
                        DetalleTreinoViewModel::class.java,
                        db
                    ) { db -> DetalleTreinoViewModel(db,exerciseDbApi) }
                )
            )
        }


    }
}

