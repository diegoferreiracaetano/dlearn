package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class BottomNavigationComponent(
    val items: List<BottomNavItem>,
    val selectedRoute: String? = null
) : Component

@Serializable
data class BottomNavItem(
    val label: String,
    val route: String,
    val iconIdentifier: String? = null
)
