package com.novacodestudios.grispisupport.presentation.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.R
import com.novacodestudios.grispisupport.presentation.component.LargeProfileCircle
import com.novacodestudios.grispisupport.presentation.model.Channel
import com.novacodestudios.grispisupport.presentation.model.Priority
import com.novacodestudios.grispisupport.presentation.model.Tag
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.TicketStatus
import com.novacodestudios.grispisupport.presentation.model.Type
import com.novacodestudios.grispisupport.presentation.model.toUiText
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.util.toColor
import com.novacodestudios.grispisupport.presentation.util.toUiName
import com.novacodestudios.grispisupport.presentation.util.user1
import com.novacodestudios.grispisupport.presentation.util.user2
import com.novacodestudios.grispisupport.presentation.util.user5
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: channel için daha uygun bir yer bulunacak
@Composable
fun TicketItem(ticket: Ticket, onClick: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("hh:mm", Locale.getDefault()) }
    val dateText = dateFormat.format(Date(ticket.updatedAt))

    val statusColor = ticket.status.toColor()
    ListItem(
        modifier = Modifier.clickable { onClick() },
        leadingContent = {
            LargeProfileCircle(
                name = ticket.requester.name,
            )
        },
        overlineContent = {
            Row {
                Text(
                    "#${ticket.number} · ${ticket.requester.name}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = ticket.channel.toUiText(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Text(text = dateText)
            }
        },

        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    ticket.subject,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .background(statusColor, shape = RoundedCornerShape(2.dp))
                        .padding(horizontal = 1.dp)
                    // .width(56.dp)
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = ticket.status.toUiName(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        supportingContent = {
            Text(ticket.lastMessageContent, maxLines = 2, overflow = TextOverflow.Ellipsis)
        },

        )
}

@Preview
@Composable
private fun TicketItemPreview() {
    GrispiSupportTheme {
        TicketItem(
            ticket = Ticket(
                id = "t6",
                number = 1,
                subject = LoremIpsum(50).values.joinToString(),
                requester = user1,
                assignee = user2,
                followers = listOf(user5),
                tags = listOf(
                    Tag(1, stringResource(R.string.tag_new))
                ),
                formId = stringResource(R.string.form_feedback),
                status = TicketStatus.PENDING,
                createdAt = System.currentTimeMillis() - 7 * 86400000,
                updatedAt = System.currentTimeMillis() - 6 * 86400000,
                lastMessageContent = LoremIpsum(50).values.joinToString(),
                channel = Channel.WHATSAPP,
                type = Type.QUESTION,
                priority = Priority.HIGH,
                formResponseId = "fr1"
            ),
            onClick = {}
        )
    }
}