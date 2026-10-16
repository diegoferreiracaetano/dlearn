package com.diegoferreiracaetano.dlearn.ui.viewmodel

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ScreenLoaderTest {
    private val first = Screen(components = emptyList())
    private val second = Screen(components = listOf(AppLoadingComponent))

    private fun TestScope.newLoader(state: MutableStateFlow<UIState<Screen>>) = ScreenLoader(this, state)

    @Test
    fun `given a source when load should go through Loading then Success`() =
        runTest {
            val state = MutableStateFlow<UIState<Screen>>(UIState.Success(second))
            val loader = newLoader(state)

            loader.load { flow { delay(100); emit(first) } }
            advanceTimeBy(50)
            assertEquals(UIState.Loading, state.value)

            advanceUntilIdle()
            assertEquals(UIState.Success(first), state.value)
        }

    @Test
    fun `given a failing source when load should map the exception to AppError`() =
        runTest {
            val state = MutableStateFlow<UIState<Screen>>(UIState.Loading)
            val error = AppError(AppErrorCode.NOT_FOUND, "Not found")

            newLoader(state).load { flow { throw AppException(error) } }
            advanceUntilIdle()

            assertEquals(UIState.Error(error), state.value)
        }

    @Test
    fun `given current content when refresh should keep it until the new one arrives`() =
        runTest {
            val state = MutableStateFlow<UIState<Screen>>(UIState.Success(first))
            val loader = newLoader(state)

            loader.refresh { flow { delay(100); emit(second) } }
            advanceTimeBy(50)
            assertEquals(UIState.Success(first), state.value)

            advanceUntilIdle()
            assertEquals(UIState.Success(second), state.value)
        }

    @Test
    fun `given a running load when a new one starts should cancel the previous`() =
        runTest {
            val state = MutableStateFlow<UIState<Screen>>(UIState.Loading)
            val loader = newLoader(state)

            loader.load { flow { delay(200); emit(first) } }
            advanceTimeBy(50)
            loader.load { flowOf(second) }
            advanceUntilIdle()

            assertEquals(UIState.Success(second), state.value)
        }

    @Test
    fun `given a refresh as the last source when retry should repeat it showing Loading`() =
        runTest {
            val state = MutableStateFlow<UIState<Screen>>(UIState.Success(first))
            val loader = newLoader(state)
            var calls = 0

            loader.refresh {
                calls++
                flow { delay(100); emit(second) }
            }
            advanceUntilIdle()

            loader.retry()
            advanceTimeBy(50)
            assertEquals(UIState.Loading, state.value)
            advanceUntilIdle()

            assertEquals(2, calls)
            assertEquals(UIState.Success(second), state.value)
        }

    @Test
    fun `given nothing loaded when retry should do nothing`() =
        runTest {
            val state = MutableStateFlow<UIState<Screen>>(UIState.Loading)

            newLoader(state).retry()
            advanceUntilIdle()

            assertEquals(UIState.Loading, state.value)
        }
}
