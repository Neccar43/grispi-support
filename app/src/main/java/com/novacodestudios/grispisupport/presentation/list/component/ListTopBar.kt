package com.novacodestudios.grispisupport.presentation.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopBar(
    onSearchClick: () -> Unit,
    onMenuClick: () -> Unit,
) {

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, null)
            }
        },
        title = { ListMenu() }, actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, null)
            }
        })
}

@Composable
fun ListMenu() {
    var expanded by remember { mutableStateOf(false) }
    var option by remember { mutableStateOf(ListMenuOption.UNSOLVED_RECORDS) }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            // horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.clickable { expanded = !expanded }
        ) {
            Text(
                text = option.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ListMenuOption.entries.forEach { newOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            newOption.title,
                            color = if (newOption == ListMenuOption.SUSPENDED_RECORDS) MaterialTheme.colorScheme.error else Color.Unspecified
                        )
                    },
                    onClick = { option = newOption;expanded = false }
                )
            }
            DropdownMenuItem(
                leadingIcon = { Icon(Icons.Default.Edit, null) },
                text = { Text("Görünümleri düzenle") },
                onClick = { }
            )
        }
    }
}

enum class ListMenuOption(val title: String) {
    UNSOLVED_RECORDS("Çözülmemiş kayıtlarınız"),
    UNASSIGNED_RECORDS("Atanmamış kayıtlar"),
    ALL_UNSOLVED_RECORDS("Tüm çözülmemiş kayıtlar"),
    RECENTLY_UPDATED("Yakında güncellenmiş kayıtlar"),
    NEW_IN_GROUPS("Gruplarınızdaki yeni kayıtlar"),
    PENDING_RECORDS("Beklemedeki kayıtlar"),
    RECENTLY_SOLVED("Yakında çözülmüş kayıtlar"),
    UNSOLVED_IN_GROUPS("Gruplarınızdaki çözülmemiş kayıtlar"),
    SUSPENDED_RECORDS("Askıya alınmış kayıtlar")
}