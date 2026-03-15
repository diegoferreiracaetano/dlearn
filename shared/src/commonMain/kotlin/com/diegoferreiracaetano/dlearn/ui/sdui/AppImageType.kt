package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
enum class AppImageType {
    SEARCH,
    EMPTY_STATE,
    UNKNOWN;
}
