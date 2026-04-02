package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class BottomNavigationComponent(
    val items: List<BottomNavItem>,
    val selectedActionUrl: String? = null,
) : Component

@Serializable
data class BottomNavItem(
    val label: String,
    val actionUrl: String,
    val icon: AppIconType? = null,
)
