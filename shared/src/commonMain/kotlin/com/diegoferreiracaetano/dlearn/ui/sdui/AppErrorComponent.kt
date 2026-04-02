package com.diegoferreiracaetano.dlearn.ui.sdui

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AppErrorComponent(
    val error: AppError? = null,
    @Transient
    val throwable: Throwable? = null,
) : Component
