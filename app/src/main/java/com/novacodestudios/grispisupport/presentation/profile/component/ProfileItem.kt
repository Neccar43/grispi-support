package com.novacodestudios.grispisupport.presentation.profile.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProfileItem(
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null
) {
    ListItem(
        modifier = Modifier.clickable(enabled = onClick != null) {
            onClick?.invoke()
        },
        headlineContent = { Text(text = title) },
        supportingContent = { Text(text = subtitle) },
    )
}

