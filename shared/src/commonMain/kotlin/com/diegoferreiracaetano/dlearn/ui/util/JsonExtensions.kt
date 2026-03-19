package com.diegoferreiracaetano.dlearn.ui.util

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    classDiscriminator = "type"
    encodeDefaults = true
}

fun String.decodeToScreen(): Screen {
    return json.decodeFromString(this)
}
