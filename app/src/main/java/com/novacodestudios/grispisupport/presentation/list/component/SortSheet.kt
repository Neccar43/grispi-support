package com.novacodestudios.grispisupport.presentation.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.list.SortOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortSheet(
    currentSortOptions: SortOptions,
    isAscending: Boolean,
    onAscendingChange: (Boolean) -> Unit,
    onSortOptionChange: (SortOptions) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        dragHandle = null,
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
        )
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Sıralama ölçütü", style = MaterialTheme.typography.titleSmall)
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 0,
                        count = 2
                    ),
                    onClick = { onAscendingChange(true) },
                    selected = isAscending,
                    label = { Text("Artan") },
                    icon = { Icon(Icons.Default.ArrowUpward, null) }
                )
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = 1,
                        count = 2
                    ),
                    onClick = { onAscendingChange(false) },
                    selected = !isAscending,
                    label = { Text("Azalan") },
                    icon = { Icon(Icons.Default.ArrowDownward, null) }
                )

            }

            HorizontalDivider()
            Column {
                SortOptions.entries.forEach { sortOptions ->
                    ListItem(
                        modifier = Modifier.clickable {
                            onSortOptionChange(sortOptions)
                            onDismiss()
                        },
                        leadingContent = {
                            Icon(
                                Icons.Default.Done,
                                null,
                                tint = if (currentSortOptions == sortOptions) LocalContentColor.current else Color.Transparent
                            )
                        },
                        headlineContent = { Text(sortOptions.title) },
                        colors = ListItemDefaults.colors(containerColor = BottomSheetDefaults.ContainerColor)
                    )
                }
            }

        }
    }
}