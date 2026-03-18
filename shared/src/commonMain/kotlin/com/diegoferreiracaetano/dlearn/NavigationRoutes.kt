package com.diegoferreiracaetano.dlearn

import com.diegoferreiracaetano.dlearn.ui.sdui.AppAction

object NavigationRoutes {
    const val APP_PREFIX = "app/"
    const val HOME = "home"
    const val WATCHLIST = "app/watchlist"
    const val FAVORITE = "app/favorite"
    const val PROFILE = "profile"
    const val SEARCH = "search"
    const val MOVIE_DETAIL = "movie_detail"
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val RESET_PASSWORD = "reset_password"
    const val VERIFY_ACCOUNT = "verify_account"
    const val CREATE_NEW_PASSWORD = "create_new_password"
    const val ONBOARDING = "onboarding"

    const val PATH_ARG = "path"
    const val PARAMS_ARG = "params"
    const val MOVIE_ID_ARG = "movieId"

    const val APP_ROUTE = "${APP_PREFIX}{$PATH_ARG}?$PARAMS_ARG={$PARAMS_ARG}"
    const val MOVIE_DETAIL_ROUTE = "$MOVIE_DETAIL/{$MOVIE_ID_ARG}"

    fun buildRoute(path: String, params: Map<String, String>? = null): String {
        val base = if (path.startsWith(APP_PREFIX)) path else "$APP_PREFIX$path"
        if (params.isNullOrEmpty()) return base
        val query = params.entries.joinToString(",") { "${it.key}:${it.value}" }
        return "$base?$PARAMS_ARG=$query"
    }

    fun fromAction(action: AppAction.Navigation): String {
        return buildRoute(action.route, action.params)
    }

    fun parseParams(paramsString: String?): Map<String, String>? {
        if (paramsString.isNullOrBlank()) return null
        return paramsString.split(",").filter { it.contains(":") }.associate {
            val parts = it.split(":")
            parts[0] to parts[1]
        }
    }

    fun extractPath(route: String?): String {
        if (route == null) return HOME
        val pathWithoutParams = route.substringBefore("?")
        return if (pathWithoutParams.startsWith(APP_PREFIX)) {
            pathWithoutParams.removePrefix(APP_PREFIX)
        } else {
            pathWithoutParams
        }
    }
}
