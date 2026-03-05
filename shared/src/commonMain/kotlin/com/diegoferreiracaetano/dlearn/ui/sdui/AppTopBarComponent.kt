package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppTopBarComponent(
    val title: String,
    val showSearch: Boolean = true,
    val searchValue: String? = null
) : Component
