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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.R

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
                text = option.toUiText(),
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
                            newOption.toUiText(),
                            color = if (newOption == ListMenuOption.SUSPENDED_RECORDS) MaterialTheme.colorScheme.error else Color.Unspecified
                        )
                    },
                    onClick = { option = newOption;expanded = false }
                )
            }
            DropdownMenuItem(
                leadingIcon = { Icon(Icons.Default.Edit, null) },
                text = { Text(stringResource(R.string.edit_views)) },
                onClick = { }
            )
        }
    }
}

enum class ListMenuOption {
    UNSOLVED_RECORDS,
    UNASSIGNED_RECORDS,
    ALL_UNSOLVED_RECORDS,
    RECENTLY_UPDATED,
    NEW_IN_GROUPS,
    PENDING_RECORDS,
    RECENTLY_SOLVED,
    UNSOLVED_IN_GROUPS,
    SUSPENDED_RECORDS
}

@Composable
fun ListMenuOption.toUiText(): String {
    return when (this) {
        ListMenuOption.UNSOLVED_RECORDS -> stringResource(R.string.unsolved_records)
        ListMenuOption.UNASSIGNED_RECORDS -> stringResource(R.string.unassigned_records)
        ListMenuOption.ALL_UNSOLVED_RECORDS -> stringResource(R.string.all_unsolved_records)
        ListMenuOption.RECENTLY_UPDATED -> stringResource(R.string.recently_updated)
        ListMenuOption.NEW_IN_GROUPS -> stringResource(R.string.new_in_groups)
        ListMenuOption.PENDING_RECORDS -> stringResource(R.string.pending_records)
        ListMenuOption.RECENTLY_SOLVED -> stringResource(R.string.recently_solved)
        ListMenuOption.UNSOLVED_IN_GROUPS -> stringResource(R.string.unsolved_in_groups)
        ListMenuOption.SUSPENDED_RECORDS -> stringResource(R.string.suspended_records)
    }
}