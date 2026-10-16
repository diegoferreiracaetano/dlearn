package com.diegoferreiracaetano.dlearn.ui.sdui

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
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
    fun `UIState Error holds app error`() {
        val error = AppError(AppErrorCode.NETWORK_ERROR, "Sem rede")
        val state = UIState.Error(error)
        assertEquals(error, state.error)
    }
}
