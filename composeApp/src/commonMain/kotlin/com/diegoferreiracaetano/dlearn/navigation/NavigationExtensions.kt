package com.diegoferreiracaetano.dlearn.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.savedstate.read
import com.diegoferreiracaetano.dlearn.NavigationRoutes

inline fun <reified T : Enum<T>> NavBackStackEntry.readEnumOrDefault(
    key: String,
    default: T,
): T =
    runCatching {
        val value = this.arguments?.read { 
            if (contains(key)) getString(key) else null 
        }.orEmpty()
        if (value.isEmpty()) default else enumValueOf<T>(value)
    }.getOrDefault(default)

inline fun <reified T> NavBackStackEntry.readOrDefault(
    key: String,
    default: T,
): T {
    val value =
        this.arguments?.read {
            if (!contains(key)) return@read null
            when (T::class) {
                String::class -> getString(key) as? T
                Int::class -> getInt(key) as? T
                Boolean::class -> getBoolean(key) as? T
                Long::class -> getLong(key) as? T
                Float::class -> getFloat(key) as? T
                else -> null
            }
        }
    return value ?: default
}

fun NavController.navigateToRoute(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.navigateToPath(path: String, params: Map<String, String>? = null) {
    val isNativeRoute = graph.findNode(path) != null
    if (isNativeRoute) {
        navigate(path)
    } else {
        val router = NavigationRoutes.buildRoute(path, params)
        navigate(router)
    }
}

val NavBackStackEntry.sduiPath: String
    get() = arguments?.read { 
        if (contains(NavigationRoutes.PATH_ARG)) getString(NavigationRoutes.PATH_ARG) else null 
    }.orEmpty()

val NavBackStackEntry.sduiParams: Map<String, String>?
    get() {
        val paramsString = readOrDefault(NavigationRoutes.PARAMS_ARG, "")
        return paramsString.takeIf { it.isNotEmpty() }?.split(",")?.associate {
            val parts = it.split(":")
            parts[0] to parts.getOrElse(1) { "" }
        }
    }
