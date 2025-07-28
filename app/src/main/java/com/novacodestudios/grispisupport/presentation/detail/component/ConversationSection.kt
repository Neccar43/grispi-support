package com.novacodestudios.grispisupport.presentation.detail.component

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novacodestudios.grispisupport.presentation.component.ProfileCircle
import com.novacodestudios.grispisupport.presentation.list.component.ReceiverMessageBubbleCard
import com.novacodestudios.grispisupport.presentation.list.component.SenderMessageBubbleCard
import com.novacodestudios.grispisupport.presentation.model.ConversationItem
import com.novacodestudios.grispisupport.presentation.model.Message
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.util.formatMessageDate
import com.novacodestudios.grispisupport.presentation.util.formatMessageTime

@Composable
fun ConversationSection(
    ticket: Ticket,
    messageList: List<Message>,
    replyValue: String,
    onReplyChange: (String) -> Unit
) {
    val itemsWithLastSender =
        remember(messageList) { groupMessagesByDateWithPreviousSender(messageList) }



    SideEffect  {
        Log.d(TAG, "itemsWithLastSender: $itemsWithLastSender")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {

            items(itemsWithLastSender, key = {(item, _) ->
                when (item) {
                    is ConversationItem.DateHeader -> "date_${item.label}"
                    is ConversationItem.MessageItem -> "message_${item.message.id}"
                }}) { (item, lastSenderId) ->
                when (item) {
                    is ConversationItem.DateHeader -> {
                        Text(
                            text = item.label,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                    }

                    is ConversationItem.MessageItem -> {
                        val isRequester = item.message.senderId == ticket.requester.id
                        val isSameSender = item.message.senderId == lastSenderId
                        val messagePadding = if (isSameSender) 0.dp else 8.dp


                        SideEffect {
                            Log.d(
                                TAG,
                                "lastSenderId: $lastSenderId  currentSenderId: ${item.message.senderId} isSame: $isSameSender padding: $messagePadding"
                            )
                        }
                        Row(
                            modifier = Modifier.padding(vertical = messagePadding)
                        ) {
                            if (!isRequester) {
                                Spacer(modifier = Modifier.weight(0.2f))
                            }
                            if (isRequester && !isSameSender) {
                                ProfileCircle(
                                    name = ticket.requester.name,
                                    size = 24.dp,
                                    fontSize = 12.sp
                                )
                            }
                            if (isRequester && isSameSender) {
                                Spacer(modifier = Modifier.width(24.dp + 8.dp))
                            }
                            val content: @Composable ColumnScope.() -> Unit = {
                                Text(
                                    text = item.message.content,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                                Text(
                                    text = formatMessageTime(item.message.sentAt),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.End,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }


                            when {
                                isRequester && !isSameSender -> {
                                    ReceiverMessageBubbleCard(
                                        modifier = Modifier.weight(0.8f),
                                        content = content
                                    )
                                }

                                !isRequester && !isSameSender -> {
                                    SenderMessageBubbleCard(
                                        modifier = Modifier.weight(0.8f),
                                        content = content
                                    )
                                }

                                else -> {
                                    Card(
                                        modifier = Modifier.weight(0.8f),
                                    ) {
                                        Column(modifier = Modifier.padding(8.dp), content = content)
                                    }
                                }
                            }



                            if (isRequester) {
                                Spacer(modifier = Modifier.weight(0.2f))
                            }
                        }
                    }
                }
            }
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RectangleShape
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Şununla cevapla ", style = MaterialTheme.typography.bodyMedium)
                    Row(
                        modifier = Modifier
                            .clickable {},
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Açık cevapla",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            null,
                            tint = MaterialTheme.colorScheme.primary,
                        )

                    }
                }
                StdBasicTextField(
                    value = replyValue,
                    onValueChange = onReplyChange,
                    placeholder = "Bir cevap yazın.."
                )
                Row {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Bolt, null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.CameraAlt, null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Attachment, null)
                    }
                    /*IconButton(onClick = {}) {
                        Icon(Icons.Default.AlternateEmail, null)
                    }*/
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = {},
                        enabled = replyValue.isNotBlank(),
                        colors = IconButtonDefaults.iconButtonColors()
                            .copy(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, null)
                    }
                }
            }

        }
    }
}

private fun groupMessagesByDateWithPreviousSender(messages: List<Message>): List<Pair<ConversationItem, String?>> {
    if (messages.isEmpty()) return emptyList()

    val sortedMessages = messages.sortedBy { it.sentAt }
    val result = mutableListOf<Pair<ConversationItem, String?>>()

    var lastDateKey: String? = null
    var lastSenderId: String? = null

    for (message in sortedMessages) {
        val currentDateKey = formatMessageDate(message.sentAt)
        if (lastDateKey != currentDateKey) {
            result.add(ConversationItem.DateHeader(currentDateKey) to null)
            lastDateKey = currentDateKey
            lastSenderId = null // Tarih değişince last sender'ı sıfırla
        }

        result.add(ConversationItem.MessageItem(message) to lastSenderId)
        lastSenderId = message.senderId
    }

    return result
}

private const val TAG = "ConversationSection"