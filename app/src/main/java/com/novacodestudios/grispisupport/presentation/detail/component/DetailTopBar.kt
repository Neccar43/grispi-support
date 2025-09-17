package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.component.LargeProfileCircle
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.toUiText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    ticket: Ticket,
    navigateUp: () -> Unit,
    onTitleClick: (String) -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.clickable { onTitleClick(ticket.requester.id) },
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LargeProfileCircle(name = ticket.requester.name)
                Column {
                    Text(
                        text = ticket.subject,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(ticket.requester.name, style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = ticket.channel.toUiText(),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

            }
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        },
    )
}