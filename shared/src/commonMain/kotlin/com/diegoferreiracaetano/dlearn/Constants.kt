package com.diegoferreiracaetano.dlearn

const val SERVER_PORT = 8080

object TmdbConstants {
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val IMAGE_W500 = "w500"
    const val IMAGE_W185 = "w185"
    
    const val SITE_YOUTUBE = "YouTube"
    const val TYPE_TRAILER = "Trailer"
    const val TYPE_TEASER = "Teaser"
    
    const val DEFAULT_REGION = "BR"
    const val LANGUAGE_PT_BR = "pt-BR"

    const val TAG_SCREEN_ERROR = "TAG_SCREEN_ERROR"
}

object WatchProviderIds {
    const val NETFLIX = 8
    const val NETFLIX_ALT = 1796
    const val DISNEY_PLUS = 337
    const val AMAZON_PRIME = 9
    const val APPLE_TV = 350
    const val HBO_MAX = 384
    const val MAX = 1899
}

object WatchProviderSchemes {
    const val NETFLIX = "nflx://"
    const val DISNEY_PLUS = "disneyplus://"
    const val AMAZON_PRIME = "primevideo://"
    const val APPLE_TV = "tv://"
    const val HBO_MAX = "hbomax://"
}

object WatchProviderPaths {
    const val NETFLIX_SEARCH = "www.netflix.com/search?q="
    const val NETFLIX_WEB_SEARCH = "https://www.netflix.com/search?q="
    
    const val DISNEY_SEARCH = "search?q="
    const val DISNEY_WEB_SEARCH = "https://www.disneyplus.com/search?q="
    
    const val AMAZON_DETAIL = "detail/"
    const val AMAZON_SEARCH = "search?phrase="
    const val AMAZON_WEB_DETAIL = "https://www.amazon.com/gp/video/detail/"
    const val AMAZON_WEB_SEARCH = "https://www.primevideo.com/search?phrase="
    
    const val APPLE_SEARCH = "search?term="
    const val APPLE_WEB_SEARCH = "https://tv.apple.com/search?term="
    
    const val HBO_SEARCH = "search?q="
    const val MAX_WEB_SEARCH = "https://www.max.com/search/"
}
