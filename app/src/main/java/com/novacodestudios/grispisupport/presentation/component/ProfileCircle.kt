package com.novacodestudios.grispisupport.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileCircle(name: String, size: Dp, fontSize: TextUnit = 20.sp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.uppercase() ?: "?",
            //color = MaterialTheme.colorScheme.onPrimaryContainer,
            color = Color.White,
            fontSize = fontSize,
            style = LocalTextStyle.current.copy(lineHeight = fontSize)
        )
    }
}

@Composable
fun LargeProfileCircle(name: String) {
    ProfileCircle(
        name = name,
        size = 40.dp
    )
}

@Composable
fun SmallProfileCircle(name: String) {
    ProfileCircle(
        name = name,
        size = 24.dp,
        fontSize = 12.sp
    )
}