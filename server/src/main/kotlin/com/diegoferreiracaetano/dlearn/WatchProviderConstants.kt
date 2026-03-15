package com.diegoferreiracaetano.dlearn

object WatchProviderIds {
    const val NETFLIX = 8
    const val NETFLIX_ALT = 1757
    const val DISNEY_PLUS = 337
    const val AMAZON_PRIME = 119
    const val APPLE_TV = 2
    const val HBO_MAX = 384
    const val MAX = 1899
}

object WatchProviderSchemes {
    const val NETFLIX = "nflx://"
    const val DISNEY_PLUS = "disneyplus://"
    const val AMAZON_PRIME = "primevideo://"
    const val APPLE_TV = "videos://"
    const val HBO_MAX = "hbomax://"
}

object WatchProviderPaths {
    const val NETFLIX_SEARCH = "search?query="
    const val NETFLIX_WEB_SEARCH = "https://www.netflix.com/search?q="
    
    const val DISNEY_SEARCH = "search?q="
    const val DISNEY_WEB_SEARCH = "https://www.disneyplus.com/search?q="
    
    const val AMAZON_DETAIL = "watch?gti="
    const val AMAZON_SEARCH = "search?phrase="
    const val AMAZON_WEB_DETAIL = "https://www.primevideo.com/detail/"
    const val AMAZON_WEB_SEARCH = "https://www.primevideo.com/search?phrase="
    
    const val APPLE_SEARCH = "search?term="
    const val APPLE_WEB_SEARCH = "https://tv.apple.com/search?term="
    
    const val HBO_SEARCH = "search?query="
    const val MAX_WEB_SEARCH = "https://www.max.com/search?q="
}
