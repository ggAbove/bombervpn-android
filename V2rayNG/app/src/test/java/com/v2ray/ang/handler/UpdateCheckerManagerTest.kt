package com.v2ray.ang.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UpdateCheckerManagerTest {
    private fun compare(first: String, second: String): Int =
        UpdateCheckerManager.compareVersions(first, second).compareTo(0)

    @Test
    fun `preview identifiers compare numerically`() {
        assertEquals(1, compare("1.0.0-preview.10", "1.0.0-preview.2"))
        assertEquals(0, compare("1.0.0-preview.1", "1.0.0-preview.1"))
    }

    @Test
    fun `stable follows preview of same core version`() {
        assertEquals(-1, compare("1.0.0-preview.1", "1.0.0"))
        assertEquals(1, compare("1.0.0", "1.0.0-preview.1"))
    }

    @Test
    fun `next preview updates previously published stable version name`() {
        assertEquals(1, compare("1.0.1-preview.1", "1.0.0"))
    }

    @Test
    fun `numeric releases retain ordering and missing zero components`() {
        assertEquals(1, compare("1.10.0", "1.9.9"))
        assertEquals(0, compare("1.0", "1.0.0"))
        assertEquals(-1, compare("1.0.0", "2.0.0"))
    }

    @Test
    fun `metadata does not affect precedence`() {
        assertEquals(0, compare("1.0.0-preview.1+build.2", "1.0.0-preview.1+build.3"))
    }

    @Test
    fun `prerelease precedence follows identifier order`() {
        val versions = listOf("1.0.0-alpha", "1.0.0-alpha.1", "1.0.0-alpha.beta",
            "1.0.0-beta", "1.0.0-beta.2", "1.0.0-beta.11", "1.0.0-rc.1", "1.0.0")
        versions.zipWithNext().forEach { (first, second) ->
            assertEquals(-1, compare(first, second), "$first < $second")
            assertEquals(1, compare(second, first), "$second > $first")
        }
    }
}
