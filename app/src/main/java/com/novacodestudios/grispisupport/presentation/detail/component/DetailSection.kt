package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.component.SipAlertDialog
import com.novacodestudios.grispisupport.presentation.detail.DetailEvent
import com.novacodestudios.grispisupport.presentation.detail.DetailState
import com.novacodestudios.grispisupport.presentation.model.Priority
import com.novacodestudios.grispisupport.presentation.model.Type
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
            if (field.condition != null && field.condition.type == ConditionType.SHOW) {
                val conditionFieldResponse =
                    state.formResponse?.responses?.find { it.fieldId == field.condition.fieldId }
                if (conditionFieldResponse == null || conditionFieldResponse.value.none { it in field.condition.expectedValues }) {
                    return@items
                }
            }
            when (field.type) {
                FieldType.CHECKBOX -> {
                    var isChecked by remember { mutableStateOf(false) }
                    DetailCheckBox(
                        title = field.label,
                        checked = isChecked,
                        onCheckedChange = { isChecked = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isChecked = !isChecked },
                        isRequired = field.isRequired(state.formResponse!!)
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
                        isRequired = field.isRequired(state.formResponse!!),
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
                        isRequired = false,
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
    onCheckedChange: (Boolean) -> Unit,
    isRequired: Boolean,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
            Text(text = title)
        }
        if (isRequired) {
            Text(
                modifier = Modifier.padding(start = 48.dp),
                text = "* Zorunlu",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }


}

@Preview
@Composable
private fun DSP() {
    GrispiSupportTheme {
        Surface {
//            DetailSection(
//                state = DetailState(
//                    ticket = dummyTicketList.first(),
//                    forms = dummyForms,
//                    selectedForm = dummyForms.get(2),
//                    formResponse = null,
//                    ticketId = "t1",
//                ),
//                onEvent = {}
//            )
        }
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
    val baseRequired: Boolean,
    val order: Int,
    val condition: FieldCondition? = null
)

/*
alana gereklilik ekleme
bir alanın bir değeri alabilmesi için başka bir alanın belirli bir değere sahip olması gerekebilir
örneğin ticket durumunın kapatıldı olması için teslim tarinin doluşturulmuş olması gerekebilir

alana koşul ekleme
bir alanın gösterilmesi için başka bir alanın belirli bir değere sahip olması gerekebilir
örneğin kullanıcı talep tipinin şifremi unuttum olması durumunda yeni şifre alanı gösterilsin

 */
data class FieldCondition(
    val fieldId: String,
    val expectedValues: List<String>,
    val type: ConditionType = ConditionType.SHOW
)

enum class ConditionType {
    SHOW,
    REQUIRE
}

fun FormField.isRequired(formResponse: FormResponse): Boolean {
    if (baseRequired) return true

    val condition = condition ?: return false

    if (condition.type != ConditionType.REQUIRE) return false

    val relatedResponse = formResponse.responses
        .find { it.fieldId == condition.fieldId }
        ?: return false

    return relatedResponse.value.any { it in condition.expectedValues }
}


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