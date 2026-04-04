package com.diegoferreiracaetano.dlearn.data.util

import kotlin.test.Test
import kotlin.test.assertEquals

class OrderTypeTest {

    @Test
    fun `OrderType values are correct`() {
        assertEquals("ASC", OrderType.ASC.name)
        assertEquals("DESC", OrderType.DESC.name)
    }

    @Test
    fun `OrderType valueOf returns correct instance`() {
        assertEquals(OrderType.ASC, OrderType.valueOf("ASC"))
        assertEquals(OrderType.DESC, OrderType.valueOf("DESC"))
    }
}
