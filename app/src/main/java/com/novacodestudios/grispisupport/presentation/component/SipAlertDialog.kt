package com.novacodestudios.grispisupport.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@Composable
fun SipAlertDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    title: String,
    text: @Composable () -> Unit,
    onConfirm: () -> Unit,
    confirmButtonText: String,
    dismissButtonText: String,
    properties: DialogProperties = DialogProperties()
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = dismissButtonText)
            }
        },
        title = { Text(text = title) },
        text = {
            text()
        },
        properties = properties
    )
}