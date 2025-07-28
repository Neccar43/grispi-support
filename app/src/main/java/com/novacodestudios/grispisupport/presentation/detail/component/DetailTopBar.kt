package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.component.ProfileCircle
import com.novacodestudios.grispisupport.presentation.model.Ticket

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(ticket: Ticket, isConversation: Boolean, navigateUp: () -> Unit) {
    TopAppBar(
        title = {
            ListItem(
                leadingContent = { ProfileCircle(name = ticket.requester.name, size = 40.dp) },
                headlineContent = {
                    Text(
                        text = if (isConversation) ticket.requester.name + " ile olan sohbet" else ticket.subject,
                        style = MaterialTheme.typography.titleSmall
                    )
                },
                supportingContent = { Text(ticket.requester.name) }
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.MoreVert, null)
            }
        }
    )
}