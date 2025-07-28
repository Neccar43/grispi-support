package com.novacodestudios.grispisupport.presentation.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTopBar() {

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, null)
            }
        },
        title = { BarMenu() }, actions = {
            IconButton(onClick = {}) {
                Icon(Icons.AutoMirrored.Filled.Sort, null)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Search, null)
            }
        })
}

// TODO: Daha iyi bir isim bul
@Composable
fun BarMenu() {
    var expanded by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("Çözülmemiş kayıtlar") }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { expanded = !expanded }
        ) {
            Text(text = title)
            Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Option 1") },
                onClick = { title = "Option 1";expanded = false }
            )
            DropdownMenuItem(
                text = { Text("Option 2") },
                onClick = { title = "Option 2";expanded = false }
            )
        }
    }
}