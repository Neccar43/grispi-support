package com.novacodestudios.grispisupport.presentation.list.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.model.Ticket

@Composable
fun TicketList(tickets: List<Ticket>, onTicketClick: (Ticket) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {

        items(items = tickets, key = { it.id }) {
            HorizontalDivider()
            TicketItem(ticket = it, onClick = { onTicketClick(it) })
        }

        item {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = "Listenin sonu",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}