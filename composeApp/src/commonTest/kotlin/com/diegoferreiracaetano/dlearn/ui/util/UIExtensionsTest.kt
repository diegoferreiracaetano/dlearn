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
    fun `given profile title type when mapping to resource should return correct resource`() {
        val type = AppStringType.PROFILE_TITLE
        val expected = Res.string.profile_title

        val actual = type.toResource()

        assertEquals(expected, actual)
    }

    @Test
    fun `given nav home type when mapping to resource should return correct resource`() {
        val type = AppStringType.NAV_HOME
        val expected = Res.string.nav_home

        val actual = type.toResource()

        assertEquals(expected, actual)
    }

    @Test
    fun `given null string type when mapping should return null`() {
        val type: AppStringType? = null
        assertNull(type.toResource())
    }

    @Test
    fun `given email text field type when mapping should return email design system type`() {
        val type = AppTextFieldType.EMAIL
        val expected = TextFieldType.EMAIL

        val actual = type.toTextFieldType()

        assertEquals(expected, actual)
    }

    @Test
    fun `given password text field type when mapping should return password design system type`() {
        val type = AppTextFieldType.PASSWORD
        val expected = TextFieldType.PASSWORD

        val actual = type.toTextFieldType()

        assertEquals(expected, actual)
    }

    @Test
    fun `given movie item component when mapping to movie item should preserve data`() {
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
