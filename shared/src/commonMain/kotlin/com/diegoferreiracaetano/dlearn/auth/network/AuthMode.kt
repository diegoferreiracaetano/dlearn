package com.diegoferreiracaetano.dlearn.auth.network

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.util.AttributeKey

private const val AUTH_MODE = "AuthMode"

enum class AuthMode {
    REQUIRED,
    OPTIONAL,
    NONE,
}

val AuthModeKey = AttributeKey<AuthMode>(AUTH_MODE)

fun HttpRequestBuilder.auth(mode: AuthMode) {
    attributes.put(AuthModeKey, mode)
}
