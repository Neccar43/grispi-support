package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.novacodestudios.grispisupport.R

@Composable
fun DetailItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    trailingContent: @Composable (() -> Unit)? = null,
    isRequired: Boolean = false
) {
    ListItem(
        overlineContent = { Text(title) },
        headlineContent = { Text(value) },
        supportingContent = {
            if (isRequired) {
                Text(
                    text = stringResource(R.string.required),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        trailingContent = trailingContent,
        modifier = modifier
    )
}