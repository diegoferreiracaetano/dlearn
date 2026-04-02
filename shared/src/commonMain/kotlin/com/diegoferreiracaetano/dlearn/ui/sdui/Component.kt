@file:OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)

package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("type")
sealed interface Component
