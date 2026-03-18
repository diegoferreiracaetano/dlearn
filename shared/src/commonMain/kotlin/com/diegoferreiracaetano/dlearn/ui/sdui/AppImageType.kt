package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
enum class AppImageType {
    SEARCH,
    WATCHLIST,
    FAVORITE,
    EMPTY_STATE,
    UNKNOWN;
}
