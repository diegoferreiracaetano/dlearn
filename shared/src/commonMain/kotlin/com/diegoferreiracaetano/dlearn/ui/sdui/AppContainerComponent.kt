package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppContainerComponent(
    val topBar: Component? = null,
    val searchBar: Component? = null,
    val chipGroup: Component? = null,
    val bottomBar: Component? = null,
    val components: List<Component> = emptyList()
) : Component
