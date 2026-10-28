package com.v2ray.ang.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.v2ray.ang.AppConfig
import com.v2ray.ang.handler.AngConfigManager
import com.v2ray.ang.util.LogUtil
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class UrlImportState { Idle, Running, Success, Failure }

/** Retains one external import across activity recreation; never persists the credential URL. */
class UrlImportViewModel(
    private val importer: suspend (String) -> Pair<Int, Int> = { AngConfigManager.importBatchConfig(it, "", false) },
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val reportFailure: (String) -> Unit = { type ->
        LogUtil.e(AppConfig.TAG, "External subscription import failed", IllegalStateException(type))
    },
) : ViewModel() {
    private val _state = MutableStateFlow(UrlImportState.Idle)
    val state = _state.asStateFlow()

    fun import(input: String?) {
        if (!_state.compareAndSet(UrlImportState.Idle, UrlImportState.Running)) return
        if (input.isNullOrBlank()) {
            _state.value = UrlImportState.Failure
            return
        }
        viewModelScope.launch(dispatcher) {
            try {
                val (profiles, subscriptions) = importer(input)
                _state.value = if (profiles + subscriptions > 0) UrlImportState.Success else UrlImportState.Failure
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                // Import exceptions can contain credentials; log only a sanitized error type.
                reportFailure(error.javaClass.simpleName)
                _state.value = UrlImportState.Failure
            }
        }
    }
}
