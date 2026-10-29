package com.v2ray.ang.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.v2ray.ang.R

private val CONNECT_CONTROL_SIZE = 176.dp
private val CONNECT_RING_SIZE = 192.dp
private val MARK_SIZE = 96.dp

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
    val hasServer = !state.selectedGuid.isNullOrEmpty()
    val headline = stringResource(when {
        state.isRunning -> R.string.bomber_connected
        !hasServer -> R.string.bomber_setup
        else -> R.string.bomber_disconnected
    })
    // One line under the headline: the next step, or the engine's own words once it has any.
    val hint = when {
        !hasServer && !state.isRunning -> stringResource(R.string.bomber_first_connection)
        state.status is MainStatus.Disconnected -> stringResource(R.string.bomber_hint_idle)
        state.status is MainStatus.Connected -> stringResource(R.string.bomber_hint_running)
        else -> displayText
    }

    Box(modifier, contentAlignment = Alignment.TopCenter) {
        Column(
            Modifier.widthIn(max = 560.dp).fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(24.dp))
            ConnectControl(
                isRunning = state.isRunning,
                enabled = hasServer || state.isRunning,
                busy = isLoading,
                onClick = { onAction(MainAction.ToggleService) },
            )
            Spacer(Modifier.height(24.dp))
            Text(headline, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(hint, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            Spacer(Modifier.height(32.dp))

            if (hasServer || state.isRunning) {
                ServerRow(
                    name = selectedName?.takeIf { it.isNotBlank() } ?: stringResource(R.string.bomber_selected_server),
                    onClick = onServers,
                )
            } else {
                FilledTonalButton(onClick = { onAction(MainAction.ImportClipboard) },
                    enabled = !isLoading, modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp)) {
                    Text(stringResource(R.string.bomber_import))
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = { onAction(MainAction.ImportQRcode) },
                    modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                    Text(stringResource(R.string.bomber_scan))
                }
            }
            Spacer(Modifier.height(4.dp))
            TextButton(onClick = { onAction(MainAction.OpenSubscriptionLink("https://t.me/openbomber_vpn_bot")) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
                Text(stringResource(R.string.bomber_account))
            }
        }
    }
}

/**
 * The one control on the home screen: a round button carrying the signal mark. Filled amber while
 * the service runs, tonal otherwise; a ring shows the engine is busy. State words live next to it.
 */
@Composable
private fun ConnectControl(isRunning: Boolean, enabled: Boolean, busy: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val container by animateColorAsState(
        when {
            isRunning -> colors.primary
            enabled -> colors.surfaceContainerHigh
            else -> colors.surfaceContainerLow
        }, label = "connectContainer",
    )
    val content by animateColorAsState(
        when {
            isRunning -> colors.onPrimary
            enabled -> colors.onSurface
            else -> colors.onSurfaceVariant.copy(alpha = 0.5f)
        }, label = "connectContent",
    )
    val actionLabel = stringResource(if (isRunning) R.string.bomber_disconnect else R.string.bomber_connect)
    val stateLabel = stringResource(if (isRunning) R.string.bomber_connected else R.string.bomber_disconnected)

    Box(Modifier.size(CONNECT_RING_SIZE), contentAlignment = Alignment.Center) {
        if (busy) {
            CircularProgressIndicator(
                Modifier.size(CONNECT_RING_SIZE),
                color = colors.primary,
                trackColor = Color.Transparent,
                strokeWidth = 3.dp,
            )
        }
        Surface(
            onClick = onClick,
            enabled = enabled,
            shape = CircleShape,
            color = container,
            contentColor = content,
            // Idle: the amber ring says "this is the action"; running: the fill already does.
            border = when {
                isRunning -> null
                enabled -> BorderStroke(2.dp, colors.primary)
                else -> BorderStroke(1.dp, colors.outlineVariant)
            },
            modifier = Modifier.size(CONNECT_CONTROL_SIZE).semantics { stateDescription = stateLabel },
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.bomber_mark), actionLabel, Modifier.size(MARK_SIZE))
            }
        }
    }
}

/** Selected server as one tappable row: name on the left, chevron on the right, opens the list. */
@Composable
private fun ServerRow(name: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
        shape = MaterialTheme.shapes.medium,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.bomber_server), style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(name, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Icon(painterResource(R.drawable.ic_chevron_right_24dp), null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
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
        Text(stringResource(R.string.bomber_refresh_hint), style = MaterialTheme.typography.bodyLarge)
        FilledTonalButton(onClick = { onAction(MainAction.UpdateSubscriptions) }, enabled = !isLoading,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(stringResource(R.string.bomber_refresh)) }
        if (isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())
        Text(stringResource(R.string.bomber_test_hint), style = MaterialTheme.typography.bodyLarge)
        OutlinedButton(onClick = { onAction(MainAction.TestCurrentServer) }, enabled = state.isRunning && !state.isTesting,
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(stringResource(R.string.bomber_test)) }
        Text(displayText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(stringResource(R.string.bomber_network_hint), style = MaterialTheme.typography.bodyLarge)
        OutlinedButton(onClick = { onNavigate(MainDestination.PerAppProxy) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.bomber_apps))
        }
        Button(onClick = { onAction(MainAction.OpenSubscriptionLink("https://t.me/openbomber_support")) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) { Text(stringResource(R.string.bomber_support)) }
        TextButton(onClick = { onNavigate(MainDestination.Logcat) }, modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(R.string.bomber_logs))
        }
        Text(stringResource(R.string.bomber_privacy_hint), style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    }
}
