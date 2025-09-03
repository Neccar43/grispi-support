package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.novacodestudios.grispisupport.presentation.model.Ticket
import com.novacodestudios.grispisupport.presentation.model.User

@Composable
fun DialogList(
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    users: List<User>,
    ticket: Ticket,
    selectedUsers: List<User> = emptyList(),
    onClick: () -> Unit
) {
    Column {
        TextField(
            value = value,
            placeholder = { Text(placeholder) },
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent
            ),
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(Icons.Default.Close, null)
                    }
                }
            }

        )
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            if (users.isEmpty() && value.isEmpty()) {
                items(selectedUsers) {
                    DialogItem(
                        headline = it.name,
                        supporting = it.email,
                        isSelected = true,
                        onClick = onClick
                    )
                }
            }


            items(items = users, key = { it.id }) { user ->
                DialogItem(
                    headline = user.name,
                    supporting = user.email,
                    isSelected = selectedUsers.any { it.id == user.id },
                    onClick = onClick
                )

            }
        }
    }
}