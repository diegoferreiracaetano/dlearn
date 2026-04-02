package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
enum class AppSnackbarType {
    SUCCESS,
    ERROR,
    WARNING,
}

@Serializable
data class AppSnackbarComponent(
    val message: String,
    val snackbarType: AppSnackbarType = AppSnackbarType.SUCCESS,
    val durationMillis: Long = 3000L,
) : Component
