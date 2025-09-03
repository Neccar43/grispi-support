package com.novacodestudios.grispisupport.presentation.notification.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTopBar(notificationSize: Int, navigateUp: () -> Unit, markAllAsRead: () -> Unit) {
    TopAppBar(
        title = { Text("Bildirimler ($notificationSize)") },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    null
                )
            }
        },
        actions = {
            IconButton(onClick = markAllAsRead, enabled = notificationSize > 0) {
                Icon(
                    Icons.Default.DoneAll,
                    null
                )
            }
        }
    )
}