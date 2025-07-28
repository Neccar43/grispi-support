package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.TicketStatus
import com.novacodestudios.grispisupport.presentation.util.toColor
import com.novacodestudios.grispisupport.presentation.util.toUiName

@Composable
fun DetailSection(ticket: Ticket, replyValue: String, onReplyChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DetailItem(title = "Kayıt numarası", value = "#${ticket.number}", trailingContent = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.ContentCopy, null)
            }
        })
        HorizontalDivider()

        DetailItem(title = "Konu", value = ticket.subject)
        HorizontalDivider()

        DetailItem(title = "Talep Eden", value = ticket.requester.name)
        HorizontalDivider()

        DetailItem(title = "Atanan", value = ticket.assignee?.name ?: "Henüz atanmadı")
        HorizontalDivider()

        DetailItem(title = "Takip edenler", value = ticket.followers.joinToString { it.name })
        HorizontalDivider()

        DetailItem(title = "Form", value = ticket.form ?: "-")
        HorizontalDivider()

        DetailItem(
            title = "Etiketler",
            value = if (ticket.tags.isNotEmpty()) ticket.tags.joinToString(", ") else "Etiket yok"
        )
        HorizontalDivider()

        DetailItem(title = "Harici URL", value = "-")
        HorizontalDivider()



        DetailItem(title = "İç içe menüler", value = "-")
        HorizontalDivider()

        var isChecked by remember { mutableStateOf(false) }
        DetailItem(
            title = "Onay kutusu yazısı",
            value = "-",
            trailingContent = { Switch(checked = isChecked, onCheckedChange = { isChecked = it }) })
        HorizontalDivider()
        ListItem(
            leadingContent = {
                Icon(Icons.Default.Bolt, null)
            },
            headlineContent = {
                StdBasicTextField(
                    value = replyValue,
                    onValueChange = onReplyChange,
                    placeholder = "Bir cevap yazın.."
                )
            },
            trailingContent = {
                var isExpanded by remember { mutableStateOf(false) }
                Box {
                    Row(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.surfaceDim,
                                shape = CircleShape
                            )
                            .padding(8.dp)
                            .clickable { isExpanded = true },
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color = ticket.status.toColor(), shape = CircleShape)
                        )
                        Text(text = ticket.status.toUiName())
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

            }
        )
    }
}