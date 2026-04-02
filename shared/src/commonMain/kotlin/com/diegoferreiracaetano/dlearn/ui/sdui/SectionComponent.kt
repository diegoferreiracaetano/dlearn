package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class SectionComponent(
    val title: String,
    val items: List<SectionItem>,
) : Component

@Serializable
data class SectionItem(
    val id: String,
    val label: String,
    val value: String? = null,
    val icon: AppIconType? = null,
    val actionUrl: String? = null,
)
