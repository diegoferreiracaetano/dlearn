package com.diegoferreiracaetano.dlearn.navigation

/**
 * Definição centralizada das rotas de navegação suportadas.
 * Segue o padrão REST-like: feature/resource
 */
object AppNavigationRoute {
    const val HOME = "home"
    const val WATCHLIST = "watchlist"
    const val FAVORITE = "favorite"
    const val PROFILE = "profile"
    const val PROFILE_EDIT = "profile/edit"
    const val PROFILE_UPDATE = "profile/update"
    const val PROFILE_CHANGE_PASSWORD = "profile/change_password"
    const val LOGOUT = "logout"
    const val SEARCH = "search"
    const val MOVIES = "movies"
    const val MEMBER = "member"
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val RESET_PASSWORD = "reset_password"
    const val VERIFY_ACCOUNT = "verify_account"
    const val CREATE_NEW_PASSWORD = "create_new_password"
    const val ONBOARDING = "onboarding"
    const val FAQ = "faq"
    const val USERS = "users"

    // Movie Actions
    const val MOVIE_FAVORITE = "v1/movie/favorite"
    const val MOVIE_WATCHLIST = "v1/movie/watchlist"

    // FAQ References
    const val FAQ_PRIVACY_POLICY = "privacy-policy"
    const val FAQ_HELP_FEEDBACK = "help-feedback"
    const val FAQ_ABOUT_US = "about-us"

    // Settings
    const val SETTINGS_NOTIFICATIONS = "settings/notifications"
    const val SETTINGS_LANGUAGE = "settings/language"
    const val SETTINGS_COUNTRY = "settings/country"
    const val SETTINGS_CLEAR_CACHE = "settings/clear_cache"


    const val APP_PREFIX = "app"
    const val ARG_PATH = "path"
    const val ARG_PARAMS = "params"
    const val ARG_ID = "id"

    const val SDUI_APP_ROUTE = "$APP_PREFIX?$ARG_PATH={$ARG_PATH}&$ARG_PARAMS={$ARG_PARAMS}&${AppQueryParam.REF}={${AppQueryParam.REF}}"
    const val MOVIE_DETAIL_ROUTE = "$MOVIES/{$ARG_ID}"
}
