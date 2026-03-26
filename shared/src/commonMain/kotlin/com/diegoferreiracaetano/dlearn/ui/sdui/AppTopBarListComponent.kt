package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppTopBarListComponent(
    val items: List<AppTopBarItem>,
    val selectedActionUrl: String
) : Component

@Serializable
data class AppTopBarItem(
    val topBar: AppTopBarComponent,
    val actionUrl: String
)
