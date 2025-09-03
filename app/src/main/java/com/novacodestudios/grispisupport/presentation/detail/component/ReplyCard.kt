package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.model.Channel
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.TicketStatus
import com.novacodestudios.grispisupport.presentation.util.toColor
import com.novacodestudios.grispisupport.presentation.util.toUiName

@Composable
fun ReplyCard(
    modifier: Modifier = Modifier,
    onReplyChange: (String) -> Unit,
    replyValue: String,
    ticket: Ticket,
    onFocusChange: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val outerPadding = remember(isFocused) { if (isFocused) 16.dp else 8.dp }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            focusRequester.requestFocus()
            onFocusChange()
        }
    }
    ElevatedCard(
        modifier = modifier,
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = outerPadding)
                .padding(top = outerPadding)
                .then(
                    if (!isFocused) Modifier.padding(bottom = outerPadding) else Modifier
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var expanded by remember { mutableStateOf(false) }
            var option by remember { mutableStateOf(Channel.PUBLIC_RESPONSE) }
            if (isFocused) {
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Şu yolla yanıtla: ", style = MaterialTheme.typography.bodyMedium)
                        Row(
                            modifier = Modifier
                                .clickable { expanded = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option.title,
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
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Channel.entries.forEach { newOption ->
                            DropdownMenuItem(
                                text = { Text(newOption.title) },
                                onClick = { option = newOption;expanded = false }
                            )
                        }
                    }
                }
                StdBasicTextField(
                    value = replyValue,
                    onValueChange = onReplyChange,
                    placeholder = "Yanıt yazın...",
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Bolt, null)
                }
                if (isFocused) {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.CameraAlt, null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Attachment, null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.AlternateEmail, null)
                    }
                }
                if (!isFocused) {
                    StdBasicTextField(
                        value = replyValue,
                        onValueChange = onReplyChange,
                        placeholder = "Yanıt yazın...",
                        modifier = Modifier.onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                isFocused = true
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                var isExpanded by remember { mutableStateOf(false) }
                Box {
                    Row(
                        modifier = Modifier
                            .clickable { isExpanded = true }
                            .background(
                                color = CardDefaults.cardColors().containerColor,
                                shape = CircleShape
                            )
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(color = ticket.status.toColor(), shape = CircleShape)
                        )
                        Text(
                            text = ticket.status.toUiName(),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(16.dp))
                    }
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        TicketStatus.entries.forEach {
                            DropdownMenuItem(
                                text = { Text(it.toUiName()) },
                                onClick = { isExpanded = false }
                            )
                        }
                    }


                }
                if (isFocused) {
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