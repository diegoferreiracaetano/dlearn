package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppSnackbarComponentTest {

    @Test
    fun `AppSnackbarComponent holds values`() {
        val component = AppSnackbarComponent(
            message = "Success",
            snackbarType = AppSnackbarType.SUCCESS,
            durationMillis = 5000L
        )
        assertEquals("Success", component.message)
        assertEquals(AppSnackbarType.SUCCESS, component.snackbarType)
        assertEquals(5000L, component.durationMillis)
    }

    @Test
    fun `AppSnackbarComponent defaults`() {
        val component = AppSnackbarComponent("Default")
        assertEquals(AppSnackbarType.SUCCESS, component.snackbarType)
        assertEquals(3000L, component.durationMillis)
    }
}
