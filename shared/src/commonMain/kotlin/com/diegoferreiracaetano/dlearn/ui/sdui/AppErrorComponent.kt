package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AppErrorComponent(
    @Transient
    val throwable: Throwable? = null
) : Component
