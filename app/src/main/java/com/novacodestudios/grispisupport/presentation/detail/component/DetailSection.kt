package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DetailSection1(
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


@Composable
fun DetailSection(
    modifier: Modifier = Modifier,
    state: DetailState,
    onEvent: (DetailEvent) -> Unit
) {
    val ticket = state.ticket ?: return
    var isSubjectDialogVisible by remember { mutableStateOf(false) }
    var isRequesterDialogVisible by remember { mutableStateOf(false) }
    var isAssigneeDialogVisible by remember { mutableStateOf(false) }
    var isFormsDialogVisible by remember { mutableStateOf(false) }
    var isPriorityDialogVisible by remember { mutableStateOf(false) }
    var isFollowersDialogVisible by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier) {
        item {
            DetailItem(
                title = "Konu",
                value = ticket.subject,
                modifier = Modifier.clickable { isSubjectDialogVisible = true }
            )
            HorizontalDivider()
        }
        item {
            DetailItem(
                title = "Talep Eden", value = ticket.requester.name,
                modifier = Modifier.clickable { isRequesterDialogVisible = true })
            HorizontalDivider()
        }
        item {
            DetailItem(
                title = "Atanan", value = ticket.assignee?.name ?: "Henüz atanmadı",
                modifier = Modifier.clickable { isAssigneeDialogVisible = true })
            HorizontalDivider()
        }
        item {
            DetailItem(
                title = "Form", value = state.selectedForm!!.name,
                modifier = Modifier.clickable { isFormsDialogVisible = true }
            )
            HorizontalDivider()
        }
        item {
            DetailItem(
                title = "Öncelik", value = ticket.priority.toUiName(),
                modifier = Modifier.clickable { isPriorityDialogVisible = true })
            HorizontalDivider()
        }
        item {
            DetailItem(
                title = "Takipçiler", value = ticket.followers.joinToString { it.name },
                modifier = Modifier.clickable { isFollowersDialogVisible = true })
            HorizontalDivider()
        }

        items(
            items = state.selectedForm?.fields ?: emptyList(),
            key = { "${state.selectedForm?.id}_${it.id}" }) { field ->
            when (field.type) {
                FieldType.CHECKBOX -> {
                    var isChecked by remember { mutableStateOf(false) }
                    DetailCheckBox(
                        title = field.label,
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isChecked = !isChecked }
                    )
                    HorizontalDivider()
                }

                else -> {
                    var value by remember(state.formResponse, field.id) {
                        mutableStateOf(
                            state.formResponse?.responses?.find { it.fieldId == field.id }?.value
                        )
                    }
                    var isDialogVisible by remember { mutableStateOf(false) }
                    DetailItem(
                        title = field.label,
                        value = value?.joinToString { it } ?: "-",
                        modifier = Modifier.clickable { isDialogVisible = true }
                    )
                    HorizontalDivider()

                    if (isDialogVisible) {
                        when (field.type) {
                            FieldType.TEXT -> {
                                var textFieldValue by remember {
                                    mutableStateOf(
                                        value?.firstOrNull() ?: "-"
                                    )
                                }
                                TextFieldDialog(
                                    title = field.label,
                                    value = textFieldValue,
                                    onValueChange = { textFieldValue = it },
                                    onDismiss = { isDialogVisible = false },
                                    onConfirm = {
                                        onEvent(
                                            DetailEvent.OnFieldResponseChange(
                                                field.id,
                                                listOf(textFieldValue)
                                            )
                                        )
                                        isDialogVisible = false
                                    }
                                )
                            }

                            FieldType.SINGLE_SELECT -> {
                                var selectedOption by remember {
                                    mutableStateOf(
                                        value?.firstOrNull() ?: "-"
                                    )
                                }
                                RadioGroupDialog(
                                    title = field.label,
                                    options = field.options ?: emptyList(),
                                    selectedOption = selectedOption,
                                    onOptionSelected = { selectedOption = it },
                                    onDismiss = { isDialogVisible = false },
                                    onConfirm = {
                                        onEvent(
                                            DetailEvent.OnFieldResponseChange(
                                                field.id,
                                                listOf(selectedOption)
                                            )
                                        )
                                        isDialogVisible = false
                                    }
                                )
                            }

                            FieldType.MULTI_SELECT -> {
                                CheckBoxDialog(
                                    title = field.label,
                                    onDismiss = { isDialogVisible = false },
                                    onConfirm = {
                                        onEvent(
                                            DetailEvent.OnFieldResponseChange(
                                                field.id,
                                                it
                                            )
                                        )
                                        isDialogVisible = false
                                    },
                                    options = field.options ?: emptyList(),
                                    selectedOptions = value ?: emptyList(),
                                )
                            }

                            FieldType.DATE -> {
                                DatePickerModalInput(
                                    onDateSelected = {
                                        val instant = Instant.ofEpochMilli(it ?: 0L)
                                        val localtime =
                                            instant.atZone(ZoneId.systemDefault()).toLocalDate()
                                                .format(
                                                    DateTimeFormatter.ofPattern("dd.MM.yyyy")
                                                )
                                        onEvent(
                                            DetailEvent.OnFieldResponseChange(
                                                field.id,
                                                listOf(localtime)
                                            )
                                        )
                                        isDialogVisible = false
                                    },
                                    onDismiss = { isDialogVisible = false },
                                    initialDateMillis = value?.firstOrNull()
                                        ?.let { parseDateToMillis(it) }
                                )
                            }

                            FieldType.CHECKBOX -> {}
                        }
                    }
                }
            }

        }
    }

    if (isSubjectDialogVisible) {
        TextFieldDialog(
            title = "Konu",
            value = ticket.subject,
            onValueChange = {},
            onDismiss = { isSubjectDialogVisible = false },
            onConfirm = {}
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

    if (isFormsDialogVisible) {
        var selectedForm by remember { mutableStateOf(state.selectedForm) }
        RadioGroupDialog(
            onDismiss = { isFormsDialogVisible = false },
            title = "Form",
            options = state.forms.map { it.name },
            selectedOption = selectedForm!!.name,
            onOptionSelected = { selectedName ->
                selectedForm = state.forms.firstOrNull { it.name == selectedName }!!
            },
            onConfirm = {
                onEvent(DetailEvent.OnFormChange(selectedForm!!))
                isFormsDialogVisible = false
            }
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

    if (isPriorityDialogVisible) {
        RadioGroupDialog(
            onDismiss = { isPriorityDialogVisible = false },
            title = "Öncelik",
            options = Priority.entries.map { it.toUiName() },
            selectedOption = ticket.priority.toUiName(),
            onOptionSelected = {
                val selectedPriority =
                    Priority.entries.first { priority -> priority.toUiName() == it }
                // onEvent(DetailEvent.OnPriorityChange(selectedPriority))
            },
            onConfirm = {}
        )
    }
}

fun parseDateToMillis(dateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val localDate = LocalDate.parse(dateString, formatter)
    val localDateTime = LocalDateTime.of(localDate, java.time.LocalTime.MIDNIGHT)
    val zoneId = ZoneId.systemDefault()
    return localDateTime.atZone(zoneId).toInstant().toEpochMilli()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    initialDateMillis: Long? = null
) {
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Input,
        initialSelectedDateMillis = initialDateMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("Tamam")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun TextFieldDialog(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    SipAlertDialog(
        modifier = Modifier,
        onDismiss = onDismiss,
        title = title,
        text = {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                )
            )
        },
        onConfirm = onConfirm,
        confirmButtonText = "Tamam",
        dismissButtonText = "İptal"
    )
}

@Composable
fun RadioGroupDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    DetailDialog(
        onDismiss = onDismiss,
        title = title,
        text = {
            DialogRadioGroup(
                options = options,
                selectedOption = selectedOption,
                onOptionSelected = onOptionSelected
            )
        },
        onConfirm = onConfirm,
    )
}

@Composable
fun CheckBoxDialog(
    onConfirm: (List<String>) -> Unit,
    onDismiss: () -> Unit,
    title: String,
    options: List<String>,
    selectedOptions: List<String>

) {
    var selectedOptions by remember { mutableStateOf(selectedOptions) }
    DetailDialog(
        onDismiss = onDismiss,
        title = title,
        text = {
            LazyColumn {
                items(options) { option ->
                    var isChecked by remember { mutableStateOf(selectedOptions.contains(option)) }
                    DetailCheckBox(
                        title = option,
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            selectedOptions = if (isChecked) {
                                selectedOptions + option
                            } else {
                                selectedOptions - option
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isChecked = !isChecked
                                selectedOptions = if (isChecked) {
                                    selectedOptions + option
                                } else {
                                    selectedOptions - option
                                }
                            }
                    )
                }
            }
        },
        onConfirm = { onConfirm(selectedOptions) },
    )
}

@Composable
fun DetailCheckBox(
    modifier: Modifier = Modifier,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(text = title)
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
/*
formlar tenat a özel olarak oluşturuluyor
ortak olan alanlar konu, talap eden, atanan, form, öncelik, takipçiler

alan tipleri
metin
seçim (tekli, çoklu)
tarih
checkbox
getForms() -> form listesi
konu -> text field
talep eden -> user select (search)
atanan -> user select (search)
form -> form select (search)
öncelik -> sabit (düşük, orta, yüksek, acil) yaptığımla aynı
takipçiler -> user select (search, multi)

-------------------------------------------
Default Form
etiketler -> multi select (search)
tür -> dinamik (soru, olay, problem, görev)

-------------------------------------------
Üyelik Hesap Formu
etiketler -> multi select (search)
üye kullanıcı adı -> text field
sonraki arama tarihi -> date picker
kullanıcı talep tipi -> single select (şifremi unuttum, Hesabıma giriş yapamıyorum, Diğer)
-------------------------------------------
Teslimat ve Kargo Formu
gönderici kurum adı -> text field
backoffice kontrolüne gönderilecek -> checkbox
sipariş durumu -> dinamik (hazırlanıyor, yolda, teslim edildi, iade edildi) ticket status değil
kargo takip kodu -> text field
başlangıç şubesi -> text field
varış şubesi -> text field
sürücü adı -> text field
sipariş numarası -> text field
ürünün kargoya verilme tarihi -> date picker

 */


data class Form(
    val id: String,
    val name: String,
    val fields: List<FormField>
)

data class FormField(
    val id: String,
    val label: String,
    val type: FieldType,
    val options: List<String>? = null,
    val required: Boolean,
)

enum class FieldType {
    TEXT,
    SINGLE_SELECT,
    MULTI_SELECT,
    DATE,
    CHECKBOX
}

data class FormResponse(
    val id: String,
    val formId: String,
    val ticketId: String,
    val responses: List<FieldResponse>
)

data class FieldResponse(
    val fieldId: String,
    val value: List<String>
)