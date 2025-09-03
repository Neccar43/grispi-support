package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.component.LargeProfileCircle
import com.novacodestudios.grispisupport.presentation.model.TicketEvent
import com.novacodestudios.grispisupport.presentation.model.TicketHistory
import com.novacodestudios.grispisupport.presentation.util.allDummyUsers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistorySection(
    modifier: Modifier,
    ticketHistories: List<TicketHistory>
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(ticketHistories) {
            HistoryCard(history = it)
        }
    }
}

@Composable
fun HistoryCard(history: TicketHistory) {
    val user = remember { allDummyUsers.find { it.id == history.authorId } } ?: return
    OutlinedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LargeProfileCircle(name = user.name)
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.bodyLarge,
                    )

                    Text(
                        text = formatHistoryDate(history.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            val (comments, events) = history.events.partition { it is TicketEvent.Comment }

            comments.firstOrNull()?.let {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),

                    ) {
                    Text(
                        text = (it as TicketEvent.Comment).body,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(12.dp)
                    )
                }

            }
            if (events.isNotEmpty()) Spacer(modifier = Modifier.size(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                events.forEach { event ->
                    EventItem(event = event as TicketEvent.FieldChange)
                }
            }

        }
    }
}

private fun formatHistoryDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

@Composable
private fun EventItem(event: TicketEvent.FieldChange) {
    Row(
        //horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(text = "${event.fieldName}: ", style = MaterialTheme.typography.labelLarge)
        event.to?.let {
            Text(text = "$it ", style = MaterialTheme.typography.labelLarge)
        }
        event.from?.let {
            Text(
                text = it,
                textDecoration = TextDecoration.LineThrough,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}