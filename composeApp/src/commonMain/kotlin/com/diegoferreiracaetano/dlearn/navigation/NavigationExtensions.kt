package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
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

fun NavController.navigateClearBackStackTo(route: String) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

@Composable
fun NavController.currentRoute(): String? {
    val navBackStackEntry by this.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

// ==========================================
// SDUI Dynamic Routing Extensions
// ==========================================

val NavBackStackEntry.sduiPath: String
    get() = arguments?.read { 
        if (contains(NavigationRoutes.PATH_ARG)) getString(NavigationRoutes.PATH_ARG) else null 
    }.orEmpty()

val NavBackStackEntry.sduiParams: Map<String, String>?
    get() = NavigationRoutes.parseParams(arguments?.read { 
        if (contains(NavigationRoutes.PARAMS_ARG)) getString(NavigationRoutes.PARAMS_ARG) else null 
    })
