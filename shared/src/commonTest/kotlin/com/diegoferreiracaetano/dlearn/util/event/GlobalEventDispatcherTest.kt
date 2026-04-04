package com.diegoferreiracaetano.dlearn.util.event

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GlobalEventDispatcherTest {

    private val subject = GlobalEventDispatcher()

    @Test
    fun `given an event when emit is called should collect that event`() = runTest {
        val event = GlobalEvent.Message("Test message", GlobalEvent.MessageType.SUCCESS)
        var capturedEvent: GlobalEvent? = null
        
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            capturedEvent = subject.events.first()
        }
        
        subject.emit(event)
        
        assertEquals(event, capturedEvent)
        job.cancel()
    }

    @Test
    fun `given an event when tryEmit is called should collect that event`() = runTest {
        val event = GlobalEvent.Navigation("home/details", mapOf("id" to "1"))
        var capturedEvent: GlobalEvent? = null
        
        val job = launch(UnconfinedTestDispatcher(testScheduler)) {
            capturedEvent = subject.events.first()
        }
        
        subject.tryEmit(event)
        
        assertEquals(event, capturedEvent)
        job.cancel()
    }
}
