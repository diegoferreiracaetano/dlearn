package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ScreenSerializationTest {

    private val json = Json { 
        ignoreUnknownKeys = true 
        classDiscriminator = "type"
    }

    @Test
    fun `should serialize and deserialize Screen with AppLoadingComponent`() {
        val screen = Screen(components = listOf(AppLoadingComponent))
        val serialized = json.encodeToString(screen)
        val deserialized = json.decodeFromString<Screen>(serialized)
        assertEquals(screen, deserialized)
    }

    @Test
    fun `should deserialize Screen from json string`() {
        val jsonString = """{"components":[{"type":"com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent"}]}"""
        val deserialized = json.decodeFromString<Screen>(jsonString)
        
        assertEquals(1, deserialized.components.size)
        assertEquals(AppLoadingComponent, deserialized.components[0])
    }

    @Test
    fun `should serialize and deserialize Screen with AppTextFieldComponent`() {
        val screen = Screen(
            components = listOf(
                AppTextFieldComponent(
                    value = "test",
                    placeholder = AppStringType.APP_NAME,
                    key = "test_key"
                )
            )
        )
        val serialized = json.encodeToString(screen)
        val deserialized = json.decodeFromString<Screen>(serialized)
        assertEquals(screen, deserialized)
    }
}
