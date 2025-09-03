package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExtensionSection(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        ExtensionCard()

    }
}

@Composable
private fun ExtensionCard() {
    OutlinedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExtensionItem(title = "E Bebek", value = "02.01.2024 09:45")
            ExtensionItem(title = "Sipariş Kodu", value = "BEBSCT12345678")
            Row {
                ExtensionItem(title = "Takip Kodu", value = "1234567890")
                Spacer(modifier = Modifier.weight(1f))
                FilledIconButton(
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                    )
                }
            }


        }
    }
}

@Composable
private fun ExtensionItem(title: String, value: String) {
    Column(
        modifier = Modifier,
        //verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.labelLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }

}

