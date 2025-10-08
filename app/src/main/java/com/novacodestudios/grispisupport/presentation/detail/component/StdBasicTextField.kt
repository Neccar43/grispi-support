package com.novacodestudios.grispisupport.presentation.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle

@Composable
fun StdBasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = TextFieldDefaults.colors().focusedPlaceholderColor,
                        fontSize = textStyle.fontSize
                    )
                }
                innerTextField()
            }

        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
    )
}

@Composable
fun MentionTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onMentionTrigger: () -> Unit,
    mentionNames: List<String>,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val defaultColor = MaterialTheme.colorScheme.onSurface

    val mentionRegex = remember(mentionNames) {
        val escaped = mentionNames.joinToString("|") { Regex.escape(it) }
        Regex("@($escaped)\\b")
    }

    val annotatedText = buildAnnotatedString {
        if (value.text.isEmpty()) return@buildAnnotatedString
        var lastIndex = 0

        for (match in mentionRegex.findAll(value.text)) {
            val start = match.range.first
            val end = match.range.last + 1

            append(value.text.substring(lastIndex, start))
            withStyle(SpanStyle(color = primaryColor)) {
                append(value.text.substring(start, end))
            }
            lastIndex = end
        }

        if (lastIndex < value.text.length) {
            withStyle(SpanStyle(color = defaultColor)) {
                append(value.text.substring(lastIndex))
            }
        }
    }

    BasicTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            if (it.text.lastOrNull() == '@') {
                onMentionTrigger()
            }
        },
        cursorBrush = SolidColor(primaryColor),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Transparent),
        modifier = modifier,
        decorationBox = { innerTextField ->
            Box {
                if (value.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = TextFieldDefaults.colors().focusedPlaceholderColor,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize
                    )
                }
                Text(text = annotatedText, color = Color.Unspecified)
                innerTextField()
            }
        }
    )
}
