package com.novacodestudios.grispisupport.presentation.detail.component

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.novacodestudios.grispisupport.presentation.component.SmallProfileCircle
import com.novacodestudios.grispisupport.presentation.list.component.ReceiverMessageBubbleCard
import com.novacodestudios.grispisupport.presentation.list.component.SenderMessageBubbleCard
import com.novacodestudios.grispisupport.presentation.model.Attachment
import com.novacodestudios.grispisupport.presentation.model.AttachmentType
import com.novacodestudios.grispisupport.presentation.model.ConversationItem
import com.novacodestudios.grispisupport.presentation.model.Message
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.util.dummyTicketList
import com.novacodestudios.grispisupport.presentation.util.formatMessageDate
import com.novacodestudios.grispisupport.presentation.util.formatMessageTime
import com.novacodestudios.grispisupport.presentation.util.messagesT1

@Composable
fun ConversationSection(
    modifier: Modifier = Modifier,
    ticket: Ticket,
    messageList: List<Message>,
) {
    val itemsWithLastSender =
        remember(messageList) { groupMessagesByDateWithPreviousSender(messageList) }

    SideEffect {
        Log.d(TAG, "itemsWithLastSender: $itemsWithLastSender")
    }
    Column(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {

            items(itemsWithLastSender, key = { (item, _) ->
                when (item) {
                    is ConversationItem.DateHeader -> "date_${item.label}"
                    is ConversationItem.MessageItem -> "message_${item.message.id}"
                }
            }) { (item, lastSenderId) ->
                when (item) {
                    is ConversationItem.DateHeader -> {
                        Text(
                            text = item.label,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                    }

                    is ConversationItem.MessageItem -> {
                        val isRequester = item.message.senderId == ticket.requester.id
                        val isSameSender = item.message.senderId == lastSenderId
                        val messagePadding = if (isSameSender) 4.dp else 12.dp

                        Row(
                            modifier = Modifier.padding(top = messagePadding)
                        ) {
                            if (!isRequester) {
                                Spacer(modifier = Modifier.weight(0.2f))
                            }
                            if (isRequester && !isSameSender) {
                                SmallProfileCircle(name = ticket.requester.name)
                            }
                            if (isRequester && isSameSender) {
                                Spacer(modifier = Modifier.width(24.dp + 8.dp))
                            }

                            // Mesaj balonlarının farklı durumları
                            MessageBubble(
                                isRequester = isRequester,
                                isSameSender = isSameSender,
                                content = { MessageContent(item) },
                                modifier = Modifier.weight(0.8f)
                            )

                            if (isRequester) {
                                Spacer(modifier = Modifier.weight(0.2f))
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.padding(top = 1.dp)) }
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun MessageContent(item: ConversationItem.MessageItem) {
    BoxWithConstraints {
        val boxWidthDp = maxWidth
        val attachments = item.message.attachments
        Column {
            when (attachments.size) {
                0 -> {

                }

                1 -> {
                    AttachmentBox(
                        modifier = Modifier.fillMaxWidth().height(boxWidthDp),
                        attachment = item.message.attachments.first(),
                    )
                }

                2 -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        attachments.forEach {
                            AttachmentBox(
                                modifier = Modifier
                                    .width(boxWidthDp/2)
                                    .height(boxWidthDp),
                                attachment = it,
                            )
                        }
                    }
                }

                3 -> {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AttachmentBox(
                            modifier = Modifier
                                .width(boxWidthDp/2)
                                .height(boxWidthDp),
                            attachment = attachments[0],

                            )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AttachmentBox(
                                modifier = Modifier
                                    .size(boxWidthDp/2),
                                attachment = attachments[1],
                            )
                            AttachmentBox(
                                modifier = Modifier
                                    .size(boxWidthDp/2),
                                attachment = attachments[2],
                            )
                        }
                    }
                }

                4 -> {
                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        attachments.forEach {
                            AttachmentBox(
                                modifier = Modifier
                                    .size((boxWidthDp/2) - 2.dp),
                                attachment = it,
                            )
                        }
                    }
                }

                else -> {
                    FlowRow(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        attachments.take(3).forEach {
                            AttachmentBox(
                                modifier = Modifier
                                    .size((boxWidthDp/2) - 3.dp),
                                attachment = it,
                            )
                        }
                        Box {
                            AttachmentBox(
                                modifier = Modifier
                                    .size((boxWidthDp/2) - 3.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                        shape = MaterialTheme.shapes.small
                                    ),
                                attachment = attachments[3],
                            )
                            Text(
                                text = "+${attachments.size - 4}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
            if (attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
            }

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
    }
}

@Composable
fun AttachmentBox(modifier: Modifier = Modifier, attachment: Attachment) {
    when (attachment.type) {
        AttachmentType.IMAGE -> {
            Image(
                painter = rememberAsyncImagePainter(attachment.url),
                contentDescription = null,
                modifier = modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.Transparent),
                contentScale = ContentScale.Crop
            )

        }

        AttachmentType.FILE -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Icon(
                    Icons.Outlined.Description,
                    null,
                    modifier = Modifier.weight(1f)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 0.dp,
                                bottomStart = 8.dp,
                                bottomEnd = 8.dp
                            )
                        )
                        .padding(4.dp),
                ) {
                    Text(
                        text = attachment.name ?: "Dosya",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = attachment.size?.let { "${it / 1024} KB" } ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    isRequester: Boolean,
    isSameSender: Boolean,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier
) {
    when {
        isRequester && !isSameSender -> {
            ReceiverMessageBubbleCard(
                modifier = modifier,
                content = content
            )
        }

        isRequester && isSameSender -> {
            Card(
                modifier = modifier,
            ) {
                Column(modifier = Modifier.padding(8.dp), content = content)
            }
        }

        !isRequester && !isSameSender -> {
            SenderMessageBubbleCard(
                modifier = modifier,
                content = content
            )
        }

        !isRequester && isSameSender -> {
            ElevatedCard(
                modifier = modifier
                    .padding(end = 8.dp),
            ) {
                Column(modifier = Modifier.padding(8.dp), content = content)
            }
        }
    }
}


@Preview
@Composable
private fun ConversationSectionPreview() {
    GrispiSupportTheme {
        Surface {
            ConversationSection(
                ticket = dummyTicketList.first(),
                messageList = messagesT1,
            )
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