package com.v2ray.ang.handler

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class BomberImportLinkParserTest {
    @Test fun `nested URL retains its escapes and plus signs`() {
        assertEquals("https://sub.bmbrvpn.cc/a%2Fb?key=a+b",
            BomberImportLinkParser.parse("bombervpn://install-sub?url=https%3A%2F%2Fsub.bmbrvpn.cc%2Fa%252Fb%3Fkey%3Da%2Bb"))
    }
    @Test fun `legacy app links and outer subscription title remain supported`() {
        assertEquals("https://example.com/sub#Bomber", BomberImportLinkParser.parse(
            "v2rayng://install-sub?url=https%3A%2F%2Fexample.com%2Fsub#Bomber"))
    }
    @Test fun `inner fragment takes precedence`() {
        assertEquals("vless://example#Inner", BomberImportLinkParser.parse(
            "bombervpn://install-config?url=vless%3A%2F%2Fexample%23Inner#Outer"))
    }
    @Test fun `rejects missing ambiguous malformed and unsupported input`() {
        listOf("", "not a URI", "https://install-sub?url=x", "bombervpn://other?url=x",
            "bombervpn://install-sub", "bombervpn://install-sub?url=", "bombervpn://install-sub?url=%XX",
            "bombervpn://install-sub?url=one&url=two").forEach { assertNull(BomberImportLinkParser.parse(it), it) }
    }
}
