package com.diegoferreiracaetano.dlearn

object NavigationRoutes {
    const val APP_PREFIX = "app"

    const val HOME = "home"
    const val WATCHLIST = "watchlist"
    const val FAVORITE = "favorite"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "profile/edit"
    const val UPDATE_PROFILE = "profile/update"
    const val CHANGE_PASSWORD = "profile/change_password"
    const val LOGOUT = "logout"
    const val SEARCH = "search"
    const val MOVIE_DETAIL = "movie_detail"
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val RESET_PASSWORD = "reset_password"
    const val VERIFY_ACCOUNT = "verify_account"
    const val CREATE_NEW_PASSWORD = "create_new_password"
    const val ONBOARDING = "onboarding"
    const val FAQ = "faq"

    // Settings Routes
    const val SETTINGS_NOTIFICATIONS = "settings/notifications"
    const val SETTINGS_LANGUAGE = "settings/language"
    const val SETTINGS_COUNTRY = "settings/country"
    const val SETTINGS_CLEAR_CACHE = "settings/clear_cache"


    const val PATH_ARG = "path"
    const val PARAMS_ARG = "params"
    const val MOVIE_ID_ARG = "movieId"
    const val FAQ_REF_ARG = "ref"

    const val APP_ROUTE = "$APP_PREFIX?$PATH_ARG={$PATH_ARG}&$PARAMS_ARG={$PARAMS_ARG}&$FAQ_REF_ARG={$FAQ_REF_ARG}"
    
    const val MOVIE_DETAIL_ROUTE = "$MOVIE_DETAIL/{$MOVIE_ID_ARG}"

    fun buildRoute(path: String, params: Map<String, String>? = null): String {
        
        val cleanPath = path.removePrefix(APP_PREFIX).removePrefix("/")
        val builder = StringBuilder("$APP_PREFIX?$PATH_ARG=$cleanPath")
        
        val otherParams = mutableMapOf<String, String>()
        params?.forEach { (key, value) ->
            if (key == FAQ_REF_ARG) {
                builder.append("&$key=$value")
            } else {
                otherParams[key] = value
            }
        }

        if (otherParams.isNotEmpty()) {
            val query = otherParams.entries.joinToString(",") { "${it.key}:${it.value}" }
            builder.append("&$PARAMS_ARG=$query")
        }
        return builder.toString()
    }

    fun extractPath(route: String?): String {
        if (route == null) return HOME
        
        if (route.startsWith("$APP_PREFIX?") || route.startsWith("?$PATH_ARG=")) {
            val queryParams = route.substringAfter("?").split("&")
            val pathParam = queryParams.find { it.startsWith("$PATH_ARG=") }
            if (pathParam != null) {
                return pathParam.substringAfter("=")
            }
        }

        return route.substringBefore("?").removePrefix(APP_PREFIX).removePrefix("/")
            .takeIf { it.isNotEmpty() } ?: HOME
    }
}
