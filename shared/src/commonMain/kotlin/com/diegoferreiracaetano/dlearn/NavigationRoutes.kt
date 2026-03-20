package com.diegoferreiracaetano.dlearn

import com.diegoferreiracaetano.dlearn.ui.sdui.AppAction

object NavigationRoutes {
    const val APP_PREFIX = "app"
    const val HOME = "home"
    const val WATCHLIST = "watchlist"
    const val FAVORITE = "favorite"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "profile/edit"
    const val UPDATE_PROFILE = "profile/update"
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

    const val APP_ROUTE = "$APP_PREFIX?$PATH_ARG={$PATH_ARG}&$PARAMS_ARG={$PARAMS_ARG}"
    
    const val MOVIE_DETAIL_ROUTE = "$MOVIE_DETAIL/{$MOVIE_ID_ARG}"

    fun buildRoute(path: String, params: Map<String, String>? = null): String {
        val cleanPath = path.removePrefix(APP_PREFIX)
        val builder = StringBuilder("$APP_PREFIX?$PATH_ARG=$cleanPath")
        
        if (!params.isNullOrEmpty()) {
            val query = params.entries.joinToString(",") { "${it.key}:${it.value}" }
            builder.append("&$PARAMS_ARG=$query")
        }
        return builder.toString()
    }

    fun fromAction(action: AppAction.Navigation): String {
        return buildRoute(action.route, action.params)
    }

    fun extractPath(route: String?): String {
        if (route == null) return HOME
        return route.substringBefore("?").removePrefix(APP_PREFIX)
    }
}
