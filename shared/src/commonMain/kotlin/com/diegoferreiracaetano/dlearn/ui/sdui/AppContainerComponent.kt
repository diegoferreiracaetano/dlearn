package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppContainerComponent(
    val topBar: AppTopBarComponent? = null,
    val searchBar: Component? = null,
    val chipGroup: ChipGroupComponent? = null,
    val bottomBar: BottomNavigationComponent? = null,
    val components: List<Component> = emptyList()
) : Component
