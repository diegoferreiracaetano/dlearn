package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UIStateTest {

    @Test
    fun `UIState Loading`() {
        val state = UIState.Loading
        assertTrue(state is UIState.Loading)
    }

    @Test
    fun `UIState Success holds value`() {
        val state = UIState.Success("Data")
        assertEquals("Data", state.data)
    }

    @Test
    fun `UIState Error holds throwable`() {
        val throwable = RuntimeException("Error")
        val state = UIState.Error(throwable)
        assertEquals(throwable, state.throwable)
    }
}
