package com.diegoferreiracaetano.dlearn.network

import kotlin.test.Test
import kotlin.test.assertEquals

class AppUserAgentTest {

    @Test
    fun `toHeader returns correctly formatted string`() {
        val userAgent = AppUserAgent("MyApp", "1.2.3", "Pixel 6")
        assertEquals("MyApp/1.2.3 (Pixel 6)", userAgent.toHeader())
    }

    @Test
    fun `fromHeader parses valid header correctly`() {
        val header = "MyApp/1.2.3 (Pixel 6)"
        val userAgent = AppUserAgent.fromHeader(header)
        assertEquals("MyApp", userAgent.appName)
        assertEquals("1.2.3", userAgent.appVersion)
        assertEquals("Pixel 6", userAgent.deviceName)
    }

    @Test
    fun `fromHeader parses header with semicolon correctly`() {
        val header = "MyApp/1.2.3 (Pixel 6; Android 13)"
        val userAgent = AppUserAgent.fromHeader(header)
        assertEquals("MyApp", userAgent.appName)
        assertEquals("1.2.3", userAgent.appVersion)
        assertEquals("Pixel 6", userAgent.deviceName)
    }

    @Test
    fun `fromHeader handles missing version`() {
        val header = "MyApp (Pixel 6)"
        val userAgent = AppUserAgent.fromHeader(header)
        assertEquals("MyApp", userAgent.appName)
        assertEquals("1.0.0", userAgent.appVersion)
        assertEquals("Pixel 6", userAgent.deviceName)
    }

    @Test
    fun `fromHeader handles missing device info`() {
        val header = "MyApp/1.2.3"
        val userAgent = AppUserAgent.fromHeader(header)
        assertEquals("MyApp", userAgent.appName)
        assertEquals("1.2.3", userAgent.appVersion)
        assertEquals("Unknown", userAgent.deviceName)
    }

    @Test
    fun `fromHeader returns default for null or blank header`() {
        val default = AppUserAgent("DLearn", "1.0.0", "Unknown")
        assertEquals(default, AppUserAgent.fromHeader(null))
        assertEquals(default, AppUserAgent.fromHeader(""))
        assertEquals(default, AppUserAgent.fromHeader("   "))
    }

    @Test
    fun `fromHeader handles malformed header`() {
        val header = "MalformedHeader"
        val userAgent = AppUserAgent.fromHeader(header)
        assertEquals("MalformedHeader", userAgent.appName)
        assertEquals("1.0.0", userAgent.appVersion)
        assertEquals("Unknown", userAgent.deviceName)
    }
}
