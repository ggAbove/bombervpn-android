package com.v2ray.ang.ui

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.lifecycleScope
import com.v2ray.ang.R
import com.v2ray.ang.extension.toast
import com.v2ray.ang.handler.BomberImportLinkParser
import com.v2ray.ang.ui.base.BaseComponentActivity
import com.v2ray.ang.ui.main.MainActivity
import kotlinx.coroutines.launch

class UrlSchemeActivity : BaseComponentActivity() {
    private val model: UrlImportViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val input = when {
            intent.action == Intent.ACTION_SEND && intent.type == "text/plain" ->
                intent.getStringExtra(Intent.EXTRA_TEXT)?.takeIf { it.isNotBlank() }
            intent.action == Intent.ACTION_VIEW ->
                intent.dataString?.let(BomberImportLinkParser::parse)
            else -> null
        }
        model.import(input)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.state.collect { state ->
                    if (state == UrlImportState.Success || state == UrlImportState.Failure) {
                        toast(if (state == UrlImportState.Success) R.string.import_subscription_success
                            else R.string.import_subscription_failure)
                        startActivity(Intent(this@UrlSchemeActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    @Composable
    override fun ScreenContent() {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
