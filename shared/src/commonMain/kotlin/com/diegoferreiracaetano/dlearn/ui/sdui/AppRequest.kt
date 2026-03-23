package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppRequest(
    val path: String,
    val params: Map<String, String>? = null,
    val metadata: Map<String, String>? = null,
    val language: String? = null,
    val country: String? = null
)
