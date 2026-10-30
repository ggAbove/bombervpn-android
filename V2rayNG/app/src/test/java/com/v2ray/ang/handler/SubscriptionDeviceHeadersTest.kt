package com.v2ray.ang.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SubscriptionDeviceHeadersTest {

    @Test
    fun sendsRemnawaveDeviceHeaders() {
        val headers = SubscriptionDeviceHeaders.build("a1b2c3d4e5f60718", "15", "Xiaomi", "23078PND5G")
        assertEquals(
            mapOf(
                "x-hwid" to "a1b2c3d4e5f60718",
                "x-device-os" to "Android",
                "x-ver-os" to "15",
                "x-device-model" to "Xiaomi 23078PND5G",
            ),
            headers,
        )
    }

    @Test
    fun noIdMeansNoHeaders() {
        assertTrue(SubscriptionDeviceHeaders.build(null, "15", "Google", "Pixel 8").isEmpty())
        assertTrue(SubscriptionDeviceHeaders.build("  ", "15", "Google", "Pixel 8").isEmpty())
    }

    @Test
    fun keepsHeaderValuesAscii() {
        val headers = SubscriptionDeviceHeaders.build("abc", null, "google", "Google Pixel 8 Про")
        assertEquals("Google Pixel 8", headers["x-device-model"])
        assertEquals(null, headers["x-ver-os"])
    }

    @Test
    fun dropsManufacturerRepeatedAsModel() {
        val headers = SubscriptionDeviceHeaders.build("abc", "14", "samsung", "SAMSUNG")
        assertEquals("SAMSUNG", headers["x-device-model"])
    }
}
