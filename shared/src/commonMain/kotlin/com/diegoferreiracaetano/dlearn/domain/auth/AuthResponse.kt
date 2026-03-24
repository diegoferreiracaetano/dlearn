package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: User? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val challengeRequired: Boolean = false,
    val screen: Screen? = null
)
