package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.novacodestudios.grispisupport.presentation.component.SipAlertDialog
import com.novacodestudios.grispisupport.presentation.detail.DetailEvent
import com.novacodestudios.grispisupport.presentation.detail.DetailState
import com.novacodestudios.grispisupport.presentation.model.Priority
import com.novacodestudios.grispisupport.presentation.model.Type
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.util.dummyTicketList
import com.novacodestudios.grispisupport.presentation.util.toUiName

@Composable
fun DetailSection(
    modifier: Modifier = Modifier,
    state: DetailState,
    onEvent: (DetailEvent) -> Unit
) {
    var isSubjectDialogVisible by remember { mutableStateOf(false) }
    var isRequesterDialogVisible by remember { mutableStateOf(false) }
    var isAssigneeDialogVisible by remember { mutableStateOf(false) }
    var isFollowersDialogVisible by remember { mutableStateOf(false) }
    var isTicketsDialogVisible by remember { mutableStateOf(false) }
    var isTypeDialogVisible by remember { mutableStateOf(false) }
    var isPriorityDialogVisible by remember { mutableStateOf(false) }
    val ticket = state.ticket ?: return
    Column(
        modifier = modifier
    )
    {
        DetailItem(title = "Kayıt numarası", value = "#${ticket.number}", trailingContent = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.ContentCopy, null)
            }
        })
        HorizontalDivider()

        DetailItem(
            title = "Konu",
            value = ticket.subject,
            modifier = Modifier.clickable { isSubjectDialogVisible = true }
        )
        HorizontalDivider()

        DetailItem(
            title = "Talep Eden", value = ticket.requester.name,
            modifier = Modifier.clickable { isRequesterDialogVisible = true })
        HorizontalDivider()

        DetailItem(
            title = "Atanan", value = ticket.assignee?.name ?: "Henüz atanmadı",
            modifier = Modifier.clickable { isAssigneeDialogVisible = true })
        HorizontalDivider()

        DetailItem(
            title = "Takipçiler", value = ticket.followers.joinToString { it.name },
            modifier = Modifier.clickable { isFollowersDialogVisible = true })
        HorizontalDivider()

//        DetailItem(title = "Form", value = ticket.form ?: "-")
//        HorizontalDivider()

        DetailItem(
            title = "Etiketler",
            value = if (ticket.tags.isNotEmpty()) ticket.tags.joinToString { it.name } else "Etiket yok",
            modifier = Modifier.clickable { isTicketsDialogVisible = true }
        )
        HorizontalDivider()
        DetailItem(
            title = "Tür", value = ticket.type.toUiName(),
            modifier = Modifier.clickable { isTypeDialogVisible = true })
        HorizontalDivider()
        DetailItem(
            title = "Öncelik", value = ticket.priority.toUiName(),
            modifier = Modifier.clickable { isPriorityDialogVisible = true })
//        HorizontalDivider()

//        DetailItem(title = "Harici URL", value = "-")
//        HorizontalDivider()
//
//
//
//        DetailItem(title = "İç içe menüler", value = "-")
//        HorizontalDivider()
//
//        var isChecked by remember { mutableStateOf(false) }
//        DetailItem(
//            title = "Onay kutusu yazısı",
//            value = "-",
//            trailingContent = { Switch(checked = isChecked, onCheckedChange = { isChecked = it }) })
    }

    if (isSubjectDialogVisible) {
        SipAlertDialog(
            modifier = Modifier,
            onDismiss = { isSubjectDialogVisible = false },
            title = "Konu",
            text = {
                TextField(
                    value = ticket.subject,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )
            },
            onConfirm = {},
            confirmButtonText = "Tamam",
            dismissButtonText = "İptal"
        )
    }

    if (isRequesterDialogVisible) {
        DetailDialog(
            onDismiss = {
                isRequesterDialogVisible = false
                onEvent(DetailEvent.OnUserQueryChange(""))
            },
            title = "Talep Eden",
            text = {
                DialogList(
                    placeholder = "Talep edeni ara",
                    users = state.searchUsers,
                    ticket = ticket,
                    onClick = {
                        isRequesterDialogVisible = false
                        onEvent(DetailEvent.OnUserQueryChange(""))
                    },
                    selectedUsers = listOf(ticket.requester),
                    value = state.userQuery,
                    onValueChange = {
                        onEvent(DetailEvent.OnUserQueryChange(it))
                    },
                )
            },
            onConfirm = {
                onEvent(DetailEvent.OnUserQueryChange(""))
            },
        )
    }

    if (isAssigneeDialogVisible) {
        DetailDialog(
            onDismiss = {
                isAssigneeDialogVisible = false
                onEvent(DetailEvent.OnUserQueryChange(""))
            },
            title = "Atanan",
            text = {
                DialogList(
                    placeholder = "Atananı ara",
                    users = state.searchUsers,
                    ticket = ticket,
                    onClick = {
                        isAssigneeDialogVisible = false
                        onEvent(DetailEvent.OnUserQueryChange(""))
                    },
                    selectedUsers = ticket.assignee?.let { listOf(it) } ?: emptyList(),
                    value = state.userQuery,
                    onValueChange = {
                        onEvent(DetailEvent.OnUserQueryChange(it))
                    },
                )
            },
            onConfirm = {
                onEvent(DetailEvent.OnUserQueryChange(""))
            },
        )
    }

    if (isFollowersDialogVisible) {
        DetailDialog(
            onDismiss = {
                isFollowersDialogVisible = false
                onEvent(DetailEvent.OnUserQueryChange(""))
            },
            title = "Takipçiler",
            text = {
                DialogList(
                    placeholder = "Takipçi ara",
                    users = state.searchUsers,
                    ticket = ticket,
                    onClick = {
                        isFollowersDialogVisible = false
                        onEvent(DetailEvent.OnUserQueryChange(""))
                    },
                    selectedUsers = ticket.followers,
                    value = state.userQuery,
                    onValueChange = {
                        onEvent(DetailEvent.OnUserQueryChange(it))
                    },
                )
            },
            onConfirm = {
                onEvent(DetailEvent.OnUserQueryChange(""))
            },
        )
    }

    if (isTicketsDialogVisible) {
        DetailDialog(
            onDismiss = { isTicketsDialogVisible = false },
            title = "Etiketler",
            text = {
                Column {
                    TextField(
                        value = state.tagQuery,
                        placeholder = { Text("Etiket arama") },
                        onValueChange = { onEvent(DetailEvent.OnTagQueryChange(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent
                        ),
                        trailingIcon = {
                            if (state.tagQuery.isNotEmpty()) {
                                IconButton(onClick = { onEvent(DetailEvent.OnTagQueryChange("")) }) {
                                    Icon(Icons.Default.Close, null)
                                }
                            }
                        }
                    )
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        if (state.searchTags.isEmpty()) {
                            items(ticket.tags) {
                                AssistChip(
                                    onClick = { },
                                    label = { Text(it.name) },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                        items(items = state.searchTags, key = { it.id }) { tag ->
                            AssistChip(
                                onClick = { },
                                label = { Text(tag.name) },
                                trailingIcon = {
                                    if (ticket.tags.any { it.id == tag.id }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null
                                        )
                                    }

                                }
                            )
                        }
                    }
                }
            },
            onConfirm = { onEvent(DetailEvent.OnTagQueryChange("")) },
        )
    }

    if (isTypeDialogVisible) {
        DetailDialog(
            onDismiss = { isTypeDialogVisible = false },
            title = "Tür",
            text = {
                var selectedType by remember { mutableStateOf(ticket.type) }
                DialogRadioGroup(
                    options = Type.entries.map { it.toUiName() },
                    selectedOption = selectedType.toUiName(),
                    onOptionSelected = {
                        selectedType = Type.entries.first { type -> type.toUiName() == it }
                        // onEvent(DetailEvent.OnTypeChange(selectedType))
                    }
                )
            },
            onConfirm = {},
        )
    }

    if (isPriorityDialogVisible) {
        DetailDialog(
            onDismiss = { isPriorityDialogVisible = false },
            title = "Öncelik",
            text = {
                var selectedPriority by remember { mutableStateOf(ticket.priority) }
                DialogRadioGroup(
                    options = Priority.entries.map { it.toUiName() },
                    selectedOption = selectedPriority.toUiName(),
                    onOptionSelected = {
                        selectedPriority =
                            Priority.entries.first { priority -> priority.toUiName() == it }
                        // onEvent(DetailEvent.OnPriorityChange(selectedPriority))
                    }
                )
            },
            onConfirm = {}
        )
    }
}

fun Priority.toUiName(): String {
    return when (this) {
        Priority.LOW -> "Düşük"
        Priority.MEDIUM -> "Orta"
        Priority.HIGH -> "Yüksek"
        Priority.URGENT -> "Acil"
    }
}

fun Type.toUiName(): String {
    return when (this) {
        Type.QUESTION -> "Soru"
        Type.INCIDENT -> "Olay"
        Type.PROBLEM -> "Problem"
        Type.TASK -> "Görev"
    }
}


// Grispi tarafı
@Composable
fun DetailSection2(
    modifier: Modifier = Modifier,
    state: DetailState,
    onEvent: (DetailEvent) -> Unit
) {
    val ticket = state.ticket ?: return
    Column(
        modifier = modifier
    ) {
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Konu",
            value = ticket.subject,
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Form",
            value = ticket.form ?: "-",
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Talep Eden",
            value = ticket.requester.name,
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Atanan",
            value = ticket.assignee?.name ?: "Henüz atanmadı",
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Gönderici Kurum Adı",
            value = "E Bebek",
        )
        HorizontalDivider()
        // checkbox gelecek backoffice kontrolune gönder
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Sipariş Durumu",
            value = ticket.status.toUiName(), // TODO: gönderi durumu olacak ticket durumu değil
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Kargo Takip Kodu",
            value = "124578965412", // TODO: gönderi takip kodu olacak
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Başlangıç Şubesi",
            value = "Esenler",
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Varış Şubesi",
            value = "Üsküdar",
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Sürücü Adı",
            value = "Ahmet Yılmaz",
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Sipariş Numarası",
            value = "#1234567890",
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Ürünün Kargoya Verilme Tarihi",
            value = "12.12.2023",
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Öncelik",
            value = ticket.priority.toUiName(),
        )
        HorizontalDivider()
        DetailItem(
            modifier = Modifier.clickable {},
            title = "Takipçiler",
            value = ticket.followers.joinToString { it.name },
        )
        HorizontalDivider()

    }
}

@Preview
@Composable
private fun DSP() {
    GrispiSupportTheme {
        DetailSection(
            state = DetailState(ticket = dummyTicketList.first()),
            onEvent = {}
        )
    }
}