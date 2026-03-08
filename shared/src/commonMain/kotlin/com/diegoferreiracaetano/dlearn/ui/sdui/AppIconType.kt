package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
enum class AppIconType {
    PERSON,
    LOCK,
    NOTIFICATIONS,
    LANGUAGE,
    PUBLIC,
    DELETE,
    POLICY,
    HELP,
    INFO,
    WORKSPACE_PREMIUM,
    UNKNOWN;

    companion object {
        fun from(value: String?): AppIconType = try {
            valueOf(value?.uppercase() ?: "UNKNOWN")
        } catch (e: Exception) {
            UNKNOWN
        }
    }
}
