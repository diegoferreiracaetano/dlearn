package com.diegoferreiracaetano.dlearn.infrastructure.util

object ServerConstants {
    object Cache {
        const val USER_REMOTE_ID_PREFIX = "user_remote_id_"
        const val USER_REMOTE_EMAIL_PREFIX = "user_remote_email_"
        const val EXPIRATION_MINUTES = 10
    }

    object TmdbEndpoints {
        const val MOVIE_POPULAR = "/movie/popular"
        const val TV_POPULAR = "/tv/popular"
        const val MOVIE_TOP_RATED = "/movie/top_rated"
        const val TV_TOP_RATED = "/tv/top_rated"
        const val MOVIE_GENRES = "/genre/movie/list"
        const val TV_GENRES = "/genre/tv/list"
        const val DISCOVER_MOVIE = "/discover/movie"
        const val DISCOVER_TV = "/discover/tv"
        const val SEARCH_MULTI = "/search/multi"

        fun movieDetail(id: String) = "/movie/$id"

        fun tvDetail(id: String) = "/tv/$id"

        fun accountStates(id: String) = "/movie/$id/account_states"

        fun tvAccountStates(id: String) = "/tv/$id/account_states"

        fun favorite(accountId: String) = "/account/$accountId/favorite"

        fun favoriteMovies(accountId: String) = "/account/$accountId/favorite/movies"

        fun favoriteTv(accountId: String) = "/account/$accountId/favorite/tv"
    }

    object HomeConfig {
        const val MAX_TOP_10 = 10
        const val MAX_POPULAR = 10
        const val MAX_CATEGORIES = 4
    }
}
