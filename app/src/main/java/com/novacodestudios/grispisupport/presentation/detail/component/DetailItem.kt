package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailItem(title: String, value: String, trailingContent: @Composable (() -> Unit)? = null) {
    ListItem(
        overlineContent = { Text(title) }, headlineContent = { Text(value) },
        trailingContent = trailingContent
    )
}