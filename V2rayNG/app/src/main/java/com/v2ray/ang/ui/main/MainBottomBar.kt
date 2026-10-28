package com.v2ray.ang.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.v2ray.ang.R

@Composable
fun MainBottomBar(
    displayText: String,
    isRunning: Boolean,
    onAction: (MainAction) -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(displayText, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Button(onClick = { onAction(MainAction.ToggleService) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)) {
            Text(stringResource(if (isRunning) R.string.bomber_disconnect else R.string.bomber_connect))
        }
    }
}
