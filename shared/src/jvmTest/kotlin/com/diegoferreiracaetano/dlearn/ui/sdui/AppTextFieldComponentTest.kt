package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppTextFieldComponentTest {

    @Test
    fun `AppTextFieldComponent holds values`() {
        val component = AppTextFieldComponent(
            value = "test",
            placeholder = AppStringType.FIELD_EMAIL,
            label = AppStringType.FIELD_FULL_NAME,
            supportingText = AppStringType.FIELD_PASSWORD,
            isError = true,
            fieldType = AppTextFieldType.EMAIL,
            key = "key"
        )
        assertEquals("test", component.value)
        assertEquals(AppStringType.FIELD_EMAIL, component.placeholder)
        assertEquals(AppStringType.FIELD_FULL_NAME, component.label)
        assertEquals(AppStringType.FIELD_PASSWORD, component.supportingText)
        assertTrue(component.isError)
        assertEquals(AppTextFieldType.EMAIL, component.fieldType)
        assertEquals("key", component.key)
    }

    @Test
    fun `AppTextFieldComponent defaults`() {
        val component = AppTextFieldComponent(
            value = "",
            placeholder = AppStringType.FIELD_EMAIL,
            key = "key"
        )
        assertNull(component.label)
        assertNull(component.supportingText)
        assertFalse(component.isError)
        assertEquals(AppTextFieldType.NONE, component.fieldType)
    }
}
