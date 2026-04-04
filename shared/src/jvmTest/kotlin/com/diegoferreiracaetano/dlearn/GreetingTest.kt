package com.diegoferreiracaetano.dlearn

import org.junit.Test
import kotlin.test.assertTrue

class GreetingTest {

    @Test
    fun testGreeting() {
        val greeting = Greeting().greet()
        assertTrue(greeting.contains("Hello"))
        assertTrue(greeting.contains("Java"))
    }
}
