package com.diegoferreiracaetano.dlearn.infrastructure.services

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InMemoryFeatureToggleServiceTest {

    private val service = InMemoryFeatureToggleService()

    @Test
    fun `given external auth sync feature when isEnabled is called should return true`() {
        assertTrue(service.isEnabled(Feature.EXTERNAL_AUTH_SYNC))
    }

    @Test
    fun `given member section feature when isEnabled is called should return false`() {
        assertFalse(service.isEnabled(Feature.MEMBER_SECTION))
    }
}
