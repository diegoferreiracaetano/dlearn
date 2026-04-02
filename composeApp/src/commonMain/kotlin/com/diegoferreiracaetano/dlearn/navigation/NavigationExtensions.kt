package com.diegoferreiracaetano.dlearn.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.savedstate.read
import com.diegoferreiracaetano.dlearn.Constants

inline fun <reified T : Enum<T>> NavBackStackEntry.readEnumOrDefault(
    key: String,
    default: T,
): T =
    runCatching {
        val value =
            this.arguments
                ?.read {
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

fun NavController.navigateToPath(
    path: String,
    params: Map<String, String>? = null,
) {
    val isNativeRoute = graph.findNode(path) != null
    if (isNativeRoute) {
        navigate(path)
    } else {
        val url = AppPath(path, params)
        val route = AppPath.invoke(AppNavigationRoute.APP_PREFIX, mapOf(AppNavigationRoute.ARG_PATH to url))
        navigate(route)
    }
}

val NavBackStackEntry.sduiPath: String
    get() =
        arguments
            ?.read {
                if (contains(AppNavigationRoute.ARG_PATH)) getString(AppNavigationRoute.ARG_PATH) else null
            }.orEmpty()

val NavBackStackEntry.sduiParams: Map<String, String>?
    get() {
        val paramsMap = mutableMapOf<String, String>()

        val paramsString = readOrDefault(AppNavigationRoute.ARG_PARAMS, Constants.EMPTY_STRING)
        if (paramsString.isNotEmpty()) {
            paramsString.split(Constants.COMMA).forEach {
                val parts = it.split(Constants.COLON)
                if (parts.size >= 2) {
                    paramsMap[parts[0]] = parts[1]
                }
            }
        }

        val request = AppPath.parse(sduiPath)
        request.params?.let { paramsMap.putAll(it) }

        val ref = readOrDefault(AppQueryParam.REF, Constants.EMPTY_STRING)
        if (ref.isNotEmpty()) {
            paramsMap[AppQueryParam.REF] = ref
        }

        return paramsMap.takeIf { it.isNotEmpty() }
    }
