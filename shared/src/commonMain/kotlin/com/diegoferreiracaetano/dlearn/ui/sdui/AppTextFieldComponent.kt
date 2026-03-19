package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
enum class AppTextFieldType {
    NONE, EMAIL, PASSWORD, PHONE
}

@Serializable
data class AppTextFieldComponent(
    val value: String,
    val placeholder: AppStringType,
    val label: AppStringType? = null,
    val supportingText: AppStringType? = null,
    val isError: Boolean = false,
    val fieldType: AppTextFieldType = AppTextFieldType.NONE,
    val key: String
) : Component
