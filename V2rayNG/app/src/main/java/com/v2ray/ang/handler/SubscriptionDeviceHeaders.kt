package com.v2ray.ang.handler

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import com.v2ray.ang.AngApplication

/**
 * Device headers sent with subscription requests, as Happ and INCY do.
 *
 * Remnawave counts devices by `x-hwid`. When the panel enforces a device limit and the
 * header is missing, it serves placeholder configs ("app not supported") instead of servers.
 * The other headers only label the device in the panel and in the bot's device list.
 */
object SubscriptionDeviceHeaders {
    const val HWID = "x-hwid"
    const val DEVICE_OS = "x-device-os"
    const val OS_VERSION = "x-ver-os"
    const val DEVICE_MODEL = "x-device-model"

    private const val MAX_VALUE_LENGTH = 64

    /** Headers for this device. ANDROID_ID is stable per signing key, so reinstalls keep one slot. */
    @SuppressLint("HardwareIds")
    fun current(): Map<String, String> {
        val androidId = try {
            Settings.Secure.getString(AngApplication.application.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (_: Exception) {
            null
        }
        return build(androidId, Build.VERSION.RELEASE, Build.MANUFACTURER, Build.MODEL)
    }

    /** Pure part of [current], covered by JVM tests. Without an id nothing is sent. */
    fun build(androidId: String?, osVersion: String?, manufacturer: String?, model: String?): Map<String, String> {
        val hwid = sanitize(androidId) ?: return emptyMap()
        val maker = sanitize(manufacturer)
        val device = sanitize(model)
        // Many vendors already prefix the model with the brand ("Google Pixel 8").
        val fullModel = when {
            device == null -> maker
            maker == null || device.startsWith(maker, ignoreCase = true) -> device
            else -> "$maker $device"
        }
        return buildMap {
            put(HWID, hwid)
            put(DEVICE_OS, "Android")
            sanitize(osVersion)?.let { put(OS_VERSION, it) }
            sanitize(fullModel)?.let { put(DEVICE_MODEL, it) }
        }
    }

    /** OkHttp rejects non-ASCII header values; keep printable ASCII only. */
    private fun sanitize(value: String?): String? =
        value?.filter { it in ' '..'~' }?.trim()?.take(MAX_VALUE_LENGTH)?.takeIf { it.isNotEmpty() }
}
