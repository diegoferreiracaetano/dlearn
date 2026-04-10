package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentSerializationTest {

    private val json = Json {
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }

    private fun <T : Component> testSerialization(component: T) {
        val screen = Screen(components = listOf(component))
        val serialized = json.encodeToString(screen)
        val deserialized = json.decodeFromString<Screen>(serialized)
        assertEquals(screen, deserialized)
    }

    @Test
    fun `test AppLoadingComponent serialization`() = testSerialization(AppLoadingComponent)

    @Test
    fun `test AppTextFieldComponent serialization`() = testSerialization(
        AppTextFieldComponent(
            value = "test",
            placeholder = AppStringType.FIELD_EMAIL,
            key = "key"
        )
    )

    @Test
    fun `test AppListComponent serialization`() = testSerialization(
        AppListComponent(
            components = listOf(AppLoadingComponent)
        )
    )

    @Test
    fun `test SectionComponent serialization`() = testSerialization(
        SectionComponent(
            title = "Title",
            items = listOf(SectionItem(id = "1", label = "Label"))
        )
    )

    @Test
    fun `test AppTopBarComponent serialization`() = testSerialization(
        AppTopBarComponent(
            title = "Title"
        )
    )

    @Test
    fun `test AppFeedbackComponent serialization`() = testSerialization(
        AppFeedbackComponent(
            title = "Title",
            description = "Description"
        )
    )

    @Test
    fun `test AppSearchBarComponent serialization`() = testSerialization(
        AppSearchBarComponent(
            placeholder = "Search",
            query = "test"
        )
    )

    @Test
    fun `test AppSwitchRowComponent serialization`() = testSerialization(
        AppSwitchRowComponent(
            title = "Title",
            isChecked = true,
            preferenceKey = "switch"
        )
    )

    @Test
    fun `test AppEmptyStateComponent serialization`() = testSerialization(
        AppEmptyStateComponent(
            title = "Title",
            description = "Description"
        )
    )

    @Test
    fun `test MovieItemComponent serialization`() = testSerialization(
        MovieItemComponent(
            id = "1",
            title = "Movie",
            imageUrl = "url"
        )
    )

    @Test
    fun `test AppEpisodeComponent serialization`() = testSerialization(
        AppEpisodeComponent(
            id = "1",
            title = "Episode",
            description = "Desc",
            imageUrl = "url",
            duration = "45m"
        )
    )
}
