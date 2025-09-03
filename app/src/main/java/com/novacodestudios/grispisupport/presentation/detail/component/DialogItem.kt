package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.novacodestudios.grispisupport.presentation.component.LargeProfileCircle

@Composable
fun DialogItem(
    headline: String,
    supporting: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        leadingContent = { LargeProfileCircle(headline) },
        headlineContent = { Text(headline) },
        supportingContent = { Text(supporting) },
        trailingContent = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
            }
        }, modifier = Modifier.clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )
    )
}