package com.diegoferreiracaetano.dlearn.util

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NumberExtensionsTest {

    @Test
    fun `Double isPositive should return correct values`() {
        assertTrue(1.0.isPositive())
        assertFalse(0.0.isPositive())
        assertFalse((-1.0).isPositive())
        assertFalse(Double.NaN.isPositive())
    }

    @Test
    fun `Float isPositive should return correct values`() {
        assertTrue(1.0f.isPositive())
        assertFalse(0.0f.isPositive())
        assertFalse((-1.0f).isPositive())
        assertFalse(Float.NaN.isPositive())
    }

    @Test
    fun `Int isPositive should return correct values`() {
        assertTrue(1.isPositive())
        assertFalse(0.isPositive())
        assertFalse((-1).isPositive())
    }
}
