package com.diegoferreiracaetano.dlearn.ui.sdui

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull

class AppErrorComponentTest {

    @Test
    fun `AppErrorComponent holds values`() {
        val error = AppError(AppErrorCode.UNKNOWN_ERROR, "Generic")
        val throwable = RuntimeException("Test")
        val component = AppErrorComponent(error, throwable)

        assertEquals(error, component.error)
        assertEquals(throwable, component.throwable)
    }

    @Test
    fun `AppErrorComponent defaults`() {
        val component = AppErrorComponent()
        assertNull(component.error)
        assertNull(component.throwable)
    }

    @Test
    fun `AppErrorComponent copy works`() {
        val error = AppError(AppErrorCode.UNKNOWN_ERROR, "Error")
        val component = AppErrorComponent(error)
        val copy = component.copy(error = null)

        assertNull(copy.error)
        assertNotSame(component, copy)
    }

    @Test
    fun `AppErrorComponent equals and hashCode`() {
        val e1 = AppError(AppErrorCode.UNKNOWN_ERROR, "E")
        val c1 = AppErrorComponent(e1)
        val c2 = AppErrorComponent(e1)
        val c3 = AppErrorComponent(null)

        assertEquals(c1, c2)
        assertEquals(c1.hashCode(), c2.hashCode())
        assertEquals(false, c1.equals(c3))
    }
}
