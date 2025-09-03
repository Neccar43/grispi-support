package com.novacodestudios.grispisupport.presentation.notification.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.component.LargeProfileCircle
import com.novacodestudios.grispisupport.presentation.model.Notification
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Uygulamada kullanılan tüm lazy listler için ortak bir composable oluştur
@Composable
fun NotificationList(
    notifications: List<Notification>,
    onNotificationClick: (Notification) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {

        items(items = notifications, key = { it.id }) {
            HorizontalDivider()
            NotificationItem(notification = it, onClick = { onNotificationClick(it) })
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = "Listenin sonu",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("hh:mm", Locale.getDefault()) }
    val dateText = dateFormat.format(Date(notification.timestamp))
    ListItem(
        modifier = Modifier.clickable { onClick() },
        leadingContent = {
            BadgedBox(
                badge = {
                    if (!notification.isRead) {
                        Badge()
                    }
                }
            ) {
                LargeProfileCircle(
                    name = notification.user.name,
                )
            }

        },
        overlineContent = {
            Row {
                Text(
                    "${notification.user.name} bir yorum ekledi",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(text = dateText)
            }
        },
        headlineContent = {
            Text(
                text = notification.ticket.subject,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = notification.ticket.lastMessageContent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
    )
}