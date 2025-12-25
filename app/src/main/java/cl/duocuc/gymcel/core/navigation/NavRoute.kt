package cl.duocuc.gymcel.core.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.composable

sealed class Segment {
    data class Static(val value: String) : Segment()
    data class Param(val name: String) : Segment() {
        val placeholder get() = "{$name}"
    }
}

class Route private constructor(
    private val segments: List<Segment>,
    private val params: List<Segment.Param>
) {

    /** Patrón final de la ruta, ejemplo: "exercise/{exerciseId}" */
    private val pattern: String = segments.joinToString("/") {
        when (it) {
            is Segment.Static -> it.value
            is Segment.Param -> it.placeholder
        }
    }


    val paramNames: List<String> get() = params.map { it.name }


    fun navArgumentSpecs(type: NavType<*> = NavType.StringType): List<NamedNavArgument> =
        params.map { p ->
            navArgument(p.name) { this.type = type }
        }


    fun extractArguments(bundle: Bundle): Map<String, String?> =
        params.associate { param ->
            param.name to bundle.getString(param.name)
        }


    fun Bundle.getParam(index: Int): String? =
        this.getString(paramNames[index])



    /** Retorna solo el patrón de la ruta */
    operator fun invoke(): String = this.toString()

    /** Versión funcional: mappers por cada param */
    operator fun invoke(
        vararg mappers: (Segment.Param) -> Any?
    ): String {
        var idx = 0
        return segments.joinToString("/") { seg ->
            when (seg) {
                is Segment.Static -> seg.value
                is Segment.Param -> {
                    val value = mappers[idx++](seg)
                        ?: error("Param '${seg.name}' returned null")
                    value.toString()
                }
            }
        }
    }

    /** Versión simple: valores directos */
    operator fun invoke(vararg values: Any?): String {
        if (values.size != params.size)
            error("Expected ${params.size} params, got ${values.size}")

        var idx = 0
        return segments.joinToString("/") { seg ->
            when (seg) {
                is Segment.Static -> seg.value
                is Segment.Param -> values[idx++].toString()
            }
        }
    }

    override fun toString(): String = pattern

    override fun equals(other: Any?) =
        if (other is String) pattern == other else super.equals(other)

    override fun hashCode(): Int = pattern.hashCode()


    class Builder {
        private val segments = mutableListOf<Segment>()
        private val params = mutableListOf<Segment.Param>()

        fun text(value: String): Builder {
            segments += Segment.Static(value)
            return this
        }

        fun param(name: String): Builder {
            val p = Segment.Param(name)
            segments += p
            params += p
            return this
        }

        fun build(): Route = Route(segments, params)
    }
}

/** DSL: route { text("exercise"); param("id") } */
fun route(build: Route.Builder.() -> Unit): Route =
    Route.Builder().apply(build).build()



fun Route.composable(
    navGraphBuilder: NavGraphBuilder,
    navType: NavType<*> = NavType.StringType,
    content: @Composable (NavBackStackEntry, List<String?>) -> Unit
) {
    val route = this

    navGraphBuilder.composable(
        route = route(),
        arguments = route.navArgumentSpecs(navType)
    ) { entry ->
        val bundle = entry.arguments
        // valores ordenados según el orden de param(...) en el DSL
        val values: List<String?> = route.paramNames.map { name ->
            bundle?.getString(name)
        }
        content(entry, values)
    }
}

fun Route.composable(
    navGraphBuilder: NavGraphBuilder,
    navType: NavType<*> = NavType.StringType,
    content: @Composable (List<String?>) -> Unit
) {
    val route = this

    navGraphBuilder.composable(
        route = route(),
        arguments = route.navArgumentSpecs(navType)
    ) { entry ->
        val bundle = entry.arguments
        val values: List<String?> = route.paramNames.map { name ->
            bundle?.getString(name)
        }
        content(values)
    }
}
