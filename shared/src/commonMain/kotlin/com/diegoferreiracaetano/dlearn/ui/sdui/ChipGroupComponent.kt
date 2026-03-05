package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("ChipGroupComponent")
data class ChipGroupComponent(
    val id: String,
    val items: List<ChipItem>
) : Component

@Serializable
data class ChipItem(
    val id: String,
    val label: String,
    val isSelected: Boolean = false,
    val hasDropDown: Boolean = false,
    val isFilter: Boolean = true
)
