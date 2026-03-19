package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppAction {
    @Serializable
    @SerialName("deeplink")
    data class Deeplink(
        val url: String,
        val params: Map<String, String>? = null
    ) : AppAction

    @Serializable
    @SerialName("navigation")
    data class Navigation(
        val route: String,
        val params: Map<String, String>? = null
    ) : AppAction

    @Serializable
    @SerialName("app_call")
    data class AppCall(
        val path: String,
        val params: Map<String, String>? = null,
        val metadata: Map<String, String>? = null,
        val onSuccess: AppAction? = null,
        val onFailure: AppAction? = null
    ) : AppAction
}
