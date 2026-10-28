package com.v2ray.ang.handler

import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/** Decodes the outer app link exactly once, preserving escapes inside the subscription URL. */
object BomberImportLinkParser {
    fun parse(link: String): String? = runCatching {
        val uri = URI(link)
        if (uri.scheme !in setOf("bombervpn", "v2rayng")) return null
        if (uri.host !in setOf("install-config", "install-sub")) return null
        val values = uri.rawQuery.orEmpty().split('&').filter { it.substringBefore('=') == "url" }
        if (values.size != 1) return null
        val value = URLDecoder.decode(values.single().substringAfter('=', ""), StandardCharsets.UTF_8.name())
        if (value.isBlank()) return null
        if ('#' in value || uri.rawFragment.isNullOrEmpty()) value else "$value#${uri.rawFragment}"
    }.getOrNull()
}
