package com.v2ray.ang.ui

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UrlImportViewModelTest {
    @Test fun `invalid input does not invoke importer`() {
        var calls = 0
        val model = UrlImportViewModel({ calls++; 1 to 0 }, Dispatchers.Unconfined)
        assertEquals(UrlImportState.Idle, model.state.value)
        model.import(" ")
        assertEquals(UrlImportState.Failure, model.state.value)
        assertEquals(0, calls)
    }

    @Test fun `recreated screen cannot start a second import`() = runBlocking {
        var calls = 0
        val pending = CompletableDeferred<Pair<Int, Int>>()
        val model = UrlImportViewModel({ calls++; pending.await() }, Dispatchers.Unconfined)
        model.import("https://example.com/sub")
        assertEquals(UrlImportState.Running, model.state.value)
        model.import("https://example.com/sub")
        assertEquals(1, calls)
        pending.complete(0 to 1)
        assertEquals(UrlImportState.Success, model.state.value)
        model.import("https://example.com/sub")
        assertEquals(1, calls)
    }

    @Test fun `empty import result reports failure`() {
        val model = UrlImportViewModel({ 0 to 0 }, Dispatchers.Unconfined)
        model.import("invalid-config")
        assertEquals(UrlImportState.Failure, model.state.value)
    }

    @Test fun `import exception exposes only error type to logging`() {
        val logged = mutableListOf<String>()
        val model = UrlImportViewModel({ throw IllegalArgumentException("private-subscription-token") },
            Dispatchers.Unconfined, logged::add)
        model.import("https://example.com/private-subscription-token")
        assertEquals(UrlImportState.Failure, model.state.value)
        assertEquals(listOf("IllegalArgumentException"), logged)
    }
}
