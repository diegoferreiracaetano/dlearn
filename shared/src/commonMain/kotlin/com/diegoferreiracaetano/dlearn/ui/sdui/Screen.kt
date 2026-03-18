package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class Screen(
    val id: String? = null,
    val showSearch: Boolean = false,
    val topBar: AppTopBarComponent? = null,
    val components: List<Component>,
)
