package com.diegoferreiracaetano.dlearn.ui.util

import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.error_invalid_credentials
import dlearn.composeapp.generated.resources.error_network
import dlearn.composeapp.generated.resources.error_unknown
import kotlin.test.Test
import kotlin.test.assertEquals

class AppErrorMapperTest {

    @Test
    fun given_network_error_code_when_mapping_to_resource_should_return_network_error_string() {
        val code = AppErrorCode.NETWORK_ERROR
        val expected = Res.string.error_network

        val actual = code.toResource()

        assertEquals(expected, actual)
    }

    @Test
    fun given_invalid_credentials_code_when_mapping_to_resource_should_return_credentials_error_string() {
        val code = AppErrorCode.INVALID_CREDENTIALS
        val expected = Res.string.error_invalid_credentials

        val actual = code.toResource()

        assertEquals(expected, actual)
    }

    @Test
    fun given_unknown_code_when_mapping_to_resource_should_return_unknown_error_string() {
        val code = AppErrorCode.UNKNOWN_ERROR
        val expected = Res.string.error_unknown

        val actual = code.toResource()

        assertEquals(expected, actual)
    }
}
