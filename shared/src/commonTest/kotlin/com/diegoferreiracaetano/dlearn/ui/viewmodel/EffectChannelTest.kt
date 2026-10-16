package com.diegoferreiracaetano.dlearn.ui.viewmodel

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EffectChannelTest {
    @Test
    fun `given effect sent before collecting when collecting should receive it`() =
        runTest {
            val channel = EffectChannel<String>()
            channel.send("navigate")

            assertEquals("navigate", channel.effects.first())
        }

    @Test
    fun `given several effects when collecting should receive them in order`() =
        runTest {
            val channel = EffectChannel<Int>()
            channel.send(1)
            channel.send(2)
            channel.send(3)

            assertEquals(listOf(1, 2, 3), channel.effects.take(3).toList())
        }

    @Test
    fun `given effect already collected when collecting again should not receive it twice`() =
        runTest {
            val channel = EffectChannel<String>()
            channel.send("first")
            assertEquals("first", channel.effects.first())

            channel.send("second")

            assertEquals("second", channel.effects.first())
        }
}
