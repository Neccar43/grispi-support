package com.novacodestudios.grispisupport.presentation.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.component.ProfileCircle
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.util.toColor
import com.novacodestudios.grispisupport.presentation.util.toUiName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TicketItem(ticket: Ticket, onClick: () -> Unit) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy hh:mm", Locale.getDefault()) }
    val dateText = dateFormat.format(Date(ticket.createdAt))

    val statusColor = ticket.status.toColor()
    ListItem(
        modifier = Modifier.clickable { onClick() },
        //headlineContent = { Text(ticket.requester.name) },
        overlineContent = {
            Row {
                Text("#${ticket.number} - ${ticket.requester.name}")
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = dateText,
                    )
                    Box(
                        modifier = Modifier
                            .background(statusColor, shape = RoundedCornerShape(2.dp))
                            .size(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = ticket.status.toUiName().first().toString(),
                            color = Color.White
                        )
                    }
                }
            }
        },
        headlineContent = { Text(ticket.subject) },
        /*supportingContent = {
            Text(ticket.subject)
        },*/
        leadingContent = {
            ProfileCircle(
                name = ticket.requester.name,
                size = 40.dp
            )
        },
        /* trailingContent = {
             Row(
                 horizontalArrangement = Arrangement.spacedBy(8.dp)
             ) {
                 Text(
                     text = dateText,
                 )
                 Box(
                     modifier = Modifier
                         .background(statusColor, shape = RoundedCornerShape(2.dp))
                         .size(16.dp),
                     contentAlignment = Alignment.Center
                 ) {
                     Text(
                         modifier = Modifier.align(Alignment.Center),
                         text = ticket.status.name.first().toString(),
                         color = Color.White
                     )
                 }
             }
         }*/
    )
}