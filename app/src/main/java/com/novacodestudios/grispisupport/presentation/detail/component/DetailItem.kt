package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DetailItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    trailingContent: @Composable (() -> Unit)? = null
) {
    ListItem(
        overlineContent = { Text(title) }, headlineContent = { Text(value) },
        trailingContent = trailingContent,
        modifier = modifier
    )
}