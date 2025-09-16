package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme

@Composable
fun ExtensionSection(modifier: Modifier) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Geowix", "T-Soft").forEach { ext ->
            item {
                var expanded by remember { mutableStateOf(false) }
                ElevatedCard {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                            .padding(16.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)

                        ) {
                            Icon(
                                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                null,
                                modifier = Modifier
                            )
                            Text(
                                text = ext,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    ExtensionCard(
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun ESP() {
    GrispiSupportTheme {
        Surface {
            ExtensionSection(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun ExtensionCard(
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier
    ) {
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
    ) {
        Text(text = title, style = MaterialTheme.typography.labelLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }

}

