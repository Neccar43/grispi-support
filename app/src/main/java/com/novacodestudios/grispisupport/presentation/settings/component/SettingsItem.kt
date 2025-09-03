package com.novacodestudios.grispisupport.presentation.settings.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingsItem(
    onClick: () -> Unit,
    headlineText: String,
    supportingText: String? = null,
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = {
            Text(
                text = headlineText,
                // style = MaterialTheme.typography.titleMedium
            )
        },
        supportingContent = {
            if (supportingText != null) {
                Text(text = supportingText)
            }
        },

        )
}