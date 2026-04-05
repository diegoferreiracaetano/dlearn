package com.diegoferreiracaetano.dlearn.ui.util

import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.TextFieldType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldType
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.nav_home
import dlearn.composeapp.generated.resources.profile_title
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UIExtensionsTest {

    @Test
    fun given_profile_title_type_when_mapping_to_resource_should_return_correct_resource() {
        val type = AppStringType.PROFILE_TITLE
        val expected = Res.string.profile_title

        val actual = type.toResource()

        assertEquals(expected, actual)
    }

    @Test
    fun given_nav_home_type_when_mapping_to_resource_should_return_correct_resource() {
        val type = AppStringType.NAV_HOME
        val expected = Res.string.nav_home

        val actual = type.toResource()

        assertEquals(expected, actual)
    }

    @Test
    fun given_null_string_type_when_mapping_should_return_null() {
        val type: AppStringType? = null
        assertNull(type.toResource())
    }

    @Test
    fun given_email_text_field_type_when_mapping_should_return_email_design_system_type() {
        val type = AppTextFieldType.EMAIL
        val expected = TextFieldType.EMAIL

        val actual = type.toTextFieldType()

        assertEquals(expected, actual)
    }

    @Test
    fun given_password_text_field_type_when_mapping_should_return_password_design_system_type() {
        val type = AppTextFieldType.PASSWORD
        val expected = TextFieldType.PASSWORD

        val actual = type.toTextFieldType()

        assertEquals(expected, actual)
    }

    @Test
    fun given_movie_item_component_when_mapping_to_movie_item_should_preserve_data() {
        val component = MovieItemComponent(
            id = "1",
            title = "Movie Title",
            imageUrl = "http://image.jpg",
            rating = "8.5",
            year = "2024",
            isPremium = true
        )

        val movieItem = component.toMovieItem()

        assertEquals(component.id, movieItem.id)
        assertEquals(component.title, movieItem.title)
        assertEquals(component.year, movieItem.year)
        assertEquals(8.5, movieItem.rating)
        assertEquals(true, movieItem.isPremium)
    }
}
