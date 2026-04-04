package com.diegoferreiracaetano.dlearn.util

import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonTest {

    @Serializable
    data class TestData(val id: Int, val name: String)

    @Test
    fun `given an object when toJson is called should return correct json string`() {
        val data = TestData(1, "Test")
        val json = data.toJson()
        assertEquals("""{"id":1,"name":"Test"}""", json)
    }

    @Test
    fun `given a json string when fromJson is called should return correct object`() {
        val json = """{"id":2,"name":"Other"}"""
        val data = json.fromJson<TestData>()
        assertEquals(TestData(2, "Other"), data)
    }
}
