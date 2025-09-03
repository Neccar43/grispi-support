package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.novacodestudios.grispisupport.presentation.component.SipAlertDialog

@Composable
fun DetailDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: @Composable () -> Unit,
) {
    SipAlertDialog(
        modifier = Modifier.padding(horizontal = 16.dp),
        onDismiss = onDismiss,
        title = title,
        text = text,
        onConfirm = onConfirm,
        confirmButtonText = "Tamam",
        dismissButtonText = "İptal",
        properties = DialogProperties(usePlatformDefaultWidth = false)
    )

}