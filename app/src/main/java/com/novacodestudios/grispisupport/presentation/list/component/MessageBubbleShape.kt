package com.novacodestudios.grispisupport.presentation.list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.util.formatMessageTime

class ReceiverMessageBubbleShape(
    private val cornerRadius: Float,

    ) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    left =25f ,
                    top = 0f,
                    right = size.width,
                    bottom = size.height,
                    topRightCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    topLeftCornerRadius = CornerRadius(0f, 0f),
                    bottomRightCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    bottomLeftCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            )

            moveTo(25f, 0f)
            lineTo(0f, 0f)
            lineTo(25f, 40f)

            close()
        }

        return Outline.Generic(path)
    }
}

class SenderMessageBubbleShape(
    private val cornerRadius: Float,

    ) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {

            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width - 25f,
                    bottom = size.height,
                    topRightCornerRadius = CornerRadius(0f, 0f),
                    topLeftCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    bottomRightCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    bottomLeftCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                )
            )
            moveTo(size.width-25f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width-25f, 40f)


            close()
        }

        return Outline.Generic(path)
    }
}


@Preview
@Composable
private fun dlkjfdls() {
    GrispiSupportTheme {
        Column {
            SenderMessageBubbleCard {
                Text(
                    text = "gkjl kj lgkfjdlk j igklj lkj ilgkşkjlk",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = formatMessageTime(System.currentTimeMillis()),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            ReceiverMessageBubbleCard {
                Text(
                    text = "gkjl kj lgkfjdlk j igklj lkj ilgkşkjlk",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = formatMessageTime(System.currentTimeMillis()),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }


    }
}

@Composable
fun ReceiverMessageBubbleCard(
    modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit
){
    Card(
        modifier = modifier,
        shape = ReceiverMessageBubbleShape(LocalDensity.current.run { 12.dp.toPx() })
    ) {
        Column(modifier = Modifier.padding(8.dp).padding(start =8.dp),content=content)
    }
}

@Composable
fun SenderMessageBubbleCard(
    modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit
){
    Card(
        modifier = modifier,
        shape = SenderMessageBubbleShape(LocalDensity.current.run { 12.dp.toPx() })
    ) {
        Column(modifier = Modifier.padding(8.dp).padding(end =8.dp),content=content)
    }
}

