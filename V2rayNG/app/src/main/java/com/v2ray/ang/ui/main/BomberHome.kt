package com.v2ray.ang.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R

/** Presentation only: all service/import operations retain MainActivity/MainViewModel ownership. */
@Composable
internal fun BomberHome(
    state: MainUiState,
    displayText: String,
    isLoading: Boolean,
    selectedName: String?,
    onAction: (MainAction) -> Unit,
    onServers: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier, contentAlignment = Alignment.TopCenter) {
    Column(
        Modifier.widthIn(max = 560.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(painterResource(R.drawable.bomber_mark), null, Modifier.size(88.dp),
            tint = if (state.isRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
        Text(stringResource(when {
            state.isRunning -> R.string.bomber_connected
            state.selectedGuid.isNullOrEmpty() -> R.string.bomber_setup
            else -> R.string.bomber_disconnected
        }),
            style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Text(displayText, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        if (isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
        Button(
            onClick = { onAction(MainAction.ToggleService) },
            enabled = state.isRunning || !state.selectedGuid.isNullOrEmpty(),
            modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
        ) { Text(stringResource(if (state.isRunning) R.string.bomber_disconnect else R.string.bomber_connect)) }
        OutlinedButton(onClick = onServers, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(selectedName?.takeIf { it.isNotBlank() } ?: stringResource(if (state.selectedGuid.isNullOrEmpty()) R.string.bomber_choose_server else R.string.bomber_selected_server),
                textAlign = TextAlign.Center)
        }
        if (state.selectedGuid.isNullOrEmpty()) {
            Text(stringResource(R.string.bomber_first_connection), style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.bomber_subscription), Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium)
        FilledTonalButton(onClick = { onAction(MainAction.ImportClipboard) },
            enabled = !isLoading, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.bomber_import))
        }
        OutlinedButton(onClick = { onAction(MainAction.ImportQRcode) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.bomber_scan))
        }
        TextButton(onClick = { onAction(MainAction.OpenSubscriptionLink("https://t.me/openbomber_vpn_bot")) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.bomber_account))
        }
    }
    }
}

@Composable
internal fun BomberHelp(
    state: MainUiState,
    displayText: String,
    isLoading: Boolean,
    onAction: (MainAction) -> Unit,
    onNavigate: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier, contentAlignment = Alignment.TopCenter) {
    Column(Modifier.widthIn(max = 560.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(stringResource(R.string.bomber_help_title), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.bomber_help_intro), color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(stringResource(R.string.bomber_refresh_hint), style = MaterialTheme.typography.bodyLarge)
        FilledTonalButton(onClick = { onAction(MainAction.UpdateSubscriptions) }, enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(stringResource(R.string.bomber_refresh)) }
        Text(stringResource(R.string.bomber_test_hint), style = MaterialTheme.typography.bodyLarge)
        OutlinedButton(onClick = { onAction(MainAction.TestCurrentServer) }, enabled = state.isRunning && !state.isTesting,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(stringResource(R.string.bomber_test)) }
        Text(displayText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(stringResource(R.string.bomber_network_hint), style = MaterialTheme.typography.bodyLarge)
        OutlinedButton(onClick = { onNavigate(MainDestination.PerAppProxy) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.bomber_apps))
        }
        TextButton(onClick = { onNavigate(MainDestination.Logcat) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.bomber_logs))
        }
        Button(onClick = { onAction(MainAction.OpenSubscriptionLink("https://t.me/openbomber_support")) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(stringResource(R.string.bomber_support)) }
        Text(stringResource(R.string.bomber_privacy_hint), style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    }
}
