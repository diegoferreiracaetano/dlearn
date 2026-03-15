package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppListComponent(
    val components: List<Component> = emptyList()
) : Component
