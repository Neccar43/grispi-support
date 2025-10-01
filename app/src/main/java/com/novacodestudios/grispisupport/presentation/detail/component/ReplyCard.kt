package com.novacodestudios.grispisupport.presentation.detail.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil3.compose.rememberAsyncImagePainter
import com.novacodestudios.grispisupport.R
import com.novacodestudios.grispisupport.presentation.component.LargeProfileCircle
import com.novacodestudios.grispisupport.presentation.detail.DetailEvent
import com.novacodestudios.grispisupport.presentation.detail.DetailState
import com.novacodestudios.grispisupport.presentation.detail.DetailTabs
import com.novacodestudios.grispisupport.presentation.model.Attachment
import com.novacodestudios.grispisupport.presentation.model.AttachmentType
import com.novacodestudios.grispisupport.presentation.model.Channel
import com.novacodestudios.grispisupport.presentation.model.TicketStatus
import com.novacodestudios.grispisupport.presentation.model.toUiText
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.theme.yellowContainer
import com.novacodestudios.grispisupport.presentation.theme.yellowOnContainer
import com.novacodestudios.grispisupport.presentation.theme.yellowPrimary
import com.novacodestudios.grispisupport.presentation.util.toColor
import com.novacodestudios.grispisupport.presentation.util.toUiName
import java.io.File
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun ReplyCard(
    modifier: Modifier = Modifier,
    state: DetailState,
    onEvent: (DetailEvent) -> Unit,
    navigateMacro: (String) -> Unit,
    onScrollToConversationPage: () -> Unit,
    onScrollToLastItem: () -> Unit,
) {
    val context = LocalContext.current
    var isFocused by remember { mutableStateOf(state.replyText.isNotBlank()) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val outerPadding = remember(isFocused) { if (isFocused) 16.dp else 0.dp }
    val ticket = state.ticket ?: return

    var selectedFiles by rememberSaveable { mutableStateOf(listOf<Uri>()) }

    val openCamera = rememberCameraLauncher { uri ->
        uri?.let {
            selectedFiles = selectedFiles + it // listeye ekle
        }
    }

    val openFilePicker = rememberFilePickerLauncher { uri ->
        uri?.let {
            selectedFiles = selectedFiles + it
        }
    }

    val (containerColor, contentColor, primary) = if (state.selectedChannel == Channel.INTERNAL_NOTE) {
        internalNoteTriple
    } else {
        val defaults = CardDefaults.elevatedCardColors()
        Triple(
            defaults.containerColor,
            defaults.contentColor,
            MaterialTheme.colorScheme.primary
        )
    }
    var isMentionMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            focusRequester.requestFocus()
            onEvent(DetailEvent.OnActiveTabChange(DetailTabs.Conversation))
            onScrollToConversationPage()
        }
    }
    LaunchedEffect(state.activeTab) {
        if (state.activeTab != DetailTabs.Conversation) {
            focusManager.clearFocus(force = true)
            isFocused = false
        } else {
            isFocused = state.replyText.isNotBlank()
            onEvent(DetailEvent.OnActiveTabChange(DetailTabs.Conversation))
            onScrollToConversationPage()
        }
    }
    Box {
        ElevatedCard(
            modifier = modifier,
            shape = RectangleShape,
            colors = CardDefaults.elevatedCardColors(
                containerColor = containerColor,
                contentColor = contentColor
            ),
        )
        {
            Column(
                modifier = Modifier
                    .padding(horizontal = outerPadding)
                    .padding(top = outerPadding)
                    .then(
                        if (!isFocused) Modifier.padding(bottom = outerPadding) else Modifier
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var expanded by remember { mutableStateOf(false) }
                if (isFocused) {
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.reply_with),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Row(
                                modifier = Modifier
                                    .clickable { expanded = true },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = state.selectedChannel.toUiText(),
                                    color = primary,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    null,
                                    tint = primary,
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            properties = PopupProperties(focusable = false),
                        ) {
                            listOf(
                                Channel.PUBLIC_RESPONSE,
                                Channel.INTERNAL_NOTE
                            ).forEach { newOption ->
                                DropdownMenuItem(
                                    text = { Text(newOption.toUiText()) },
                                    onClick = {
                                        onEvent(DetailEvent.OnChannelChange(newOption)); expanded =
                                        false
                                    }
                                )
                            }
                        }
                    }
                    StdBasicTextField(
                        value = state.replyText,
                        onValueChange = {
                            if (it.lastOrNull() == '@') {
                                isMentionMenuExpanded = true
                            }
                            onEvent(DetailEvent.OnReplyTextChange(it))
                        },
                        placeholder = stringResource(R.string.write_response),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .fillMaxWidth(),
                    )
                    if (selectedFiles.isNotEmpty()) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(selectedFiles) { uri ->
                                val mimeType = context.getMimeType(uri) ?: ""
                                if (mimeType.startsWith("image/")) {
                                    ImageWithCloseButton(
                                        imageUri = uri,
                                        onClose = { selectedFiles = selectedFiles - uri },
                                        onClickImage = { }
                                    )
                                } else {
                                    FileChipWithCloseButton(
                                        fileUri = uri,
                                        onClose = { selectedFiles = selectedFiles - uri },
                                        onClickFile = { }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (isFocused) {
                Spacer(modifier = Modifier.size(16.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navigateMacro(ticket.id)
                }) {
                    Icon(Icons.Default.Bolt, null)
                }
                if (isFocused) {
                    IconButton(onClick = {
                        openCamera()
                    }) {
                        Icon(Icons.Default.CameraAlt, null)
                    }
                    IconButton(onClick = {
                        openFilePicker()
                    }) {
                        Icon(Icons.Default.Attachment, null)
                    }
                    IconButton(onClick = {
                        // TODO: @Kişi kısmı primary color olacak
                        isMentionMenuExpanded = true
                        onEvent(DetailEvent.OnReplyTextChange(state.replyText + "@"))
                    }) {
                        Icon(Icons.Default.AlternateEmail, null)
                    }
                }
                if (!isFocused) {
                    StdBasicTextField(
                        value = state.replyText,
                        onValueChange = { onEvent(DetailEvent.OnReplyTextChange(it)) },
                        placeholder = stringResource(R.string.write_response),
                        modifier = Modifier
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    isFocused = true
                                }
                            }
                            .fillMaxWidth(),
                        singleLine = true,
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                var isExpanded by remember { mutableStateOf(false) }
                Box {
                    Row(
                        modifier = Modifier
                            .clickable { isExpanded = true }
                            .background(
                                color = if (state.selectedChannel == Channel.INTERNAL_NOTE) Color(
                                    0xFFedddc6
                                ) else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(color = ticket.status.toColor(), shape = CircleShape)
                        )
                        Text(
                            text = ticket.status.toUiName(),
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(16.dp))
                    }
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                        properties = PopupProperties(focusable = false),
                    ) {
                        TicketStatus.entries.forEach {
                            DropdownMenuItem(
                                text = { Text(it.toUiName()) },
                                onClick = {
                                    onEvent(DetailEvent.OnStatusChange(it)); isExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(end = 8.dp))
                if (isFocused && state.replyText.isNotBlank()) {
                    BadgedBox(
                        badge = {
                            if (state.numberOfChange > 0) {
                                Badge {
                                    Text(state.numberOfChange.toString())
                                }
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        FilledIconButton(
                            onClick = {
                                onEvent(
                                    DetailEvent.OnSendReply(
                                        selectedFiles.map { it.toAttachment(context) }
                                    ))
                                //  isFocused = false
                                selectedFiles = emptyList()
                                onScrollToLastItem()
                            },
                            enabled = state.replyText.isNotBlank(),
                            colors = IconButtonDefaults.filledIconButtonColors()
                                .copy(containerColor = primary)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
        DropdownMenu(
            expanded = isMentionMenuExpanded,
            onDismissRequest = { isMentionMenuExpanded = false },
            modifier = Modifier.fillMaxWidth(),
            properties = PopupProperties(focusable = false)
        ) {
            state.agentUser.forEach {
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        ListItem(
                            leadingContent = { LargeProfileCircle(it.name) },
                            headlineContent = { Text(it.name) },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                        )

                    },
                    onClick = {
                        onEvent(DetailEvent.OnReplyTextChange(state.replyText + it.name))
                        isMentionMenuExpanded = false
                    }
                )
            }
        }
    }
}

fun Context.getMimeType(uri: Uri): String? {
    return contentResolver.getType(uri)
}

@Preview
@Composable
private fun RCP() {
    GrispiSupportTheme {
        ReplyCard(
            state = DetailState(
                //ticket = dummyTicketList.first(),
                replyText = "Merhaba, size nasıl yardımcı olabilirim?",
                //selectedChannel = Channel.INTERNAL_NOTE
                ticketId = "t1",
            ),
            onEvent = {},
            navigateMacro = {},
            onScrollToConversationPage = {},
            onScrollToLastItem = {}
        )
    }

}

@Composable
fun rememberCameraLauncher(
    onImageCaptured: (Uri?) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                Toast.makeText(
                    context,
                    context.getString(R.string.camera_permission_required),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    var currentUri: Uri? by remember { mutableStateOf(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) onImageCaptured(currentUri) else onImageCaptured(null)
        }
    )

    return {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val imageFile = createImageFile(context)
            currentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                imageFile
            )
            cameraLauncher.launch(currentUri!!)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    return File(context.cacheDir, "captured_image_$timeStamp.jpg")
}

@Composable
fun ImageWithCloseButton(
    imageUri: Uri,
    onClose: () -> Unit,
    onClickImage: () -> Unit
) {
    BadgedBox(
        modifier = Modifier
            .height(60.dp),
        badge = {
            FilledIconButton(
                onClick = onClose,
                modifier = Modifier.size(16.dp),
            ) {
                Icon(
                    Icons.Default.Close,
                    null,
                )
            }
        }
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onClickImage() }
        )
    }
}

@Composable
fun rememberFilePickerLauncher(
    onFilePicked: (Uri?) -> Unit
): () -> Unit {
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onFilePicked(uri)
        }
    )

    return {
        filePickerLauncher.launch("*/*")
    }
}

@Composable
fun FileChipWithCloseButton(
    fileUri: Uri,
    onClose: () -> Unit,
    onClickFile: () -> Unit
) {
    val context = LocalContext.current
    val meta = remember(fileUri) { queryFileMeta(context, fileUri) }
    val fileName = meta?.first ?: (fileUri.lastPathSegment ?: stringResource(R.string.file))
    val fileSize = formatFileSize(meta?.second ?: -1L)
    BadgedBox(
        modifier = Modifier
            .height(60.dp),
        badge = {
            FilledIconButton(
                onClick = onClose,
                modifier = Modifier.size(16.dp),
            ) {
                Icon(
                    Icons.Default.Close,
                    null,
                )
            }
        }
    ) {
        OutlinedCard(
            modifier = Modifier
                .widthIn(min = 120.dp, max = 180.dp) // dar-uzun kontrolü
                .clickable { onClickFile() }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = fileName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = fileSize,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


fun queryFileMeta(context: Context, uri: Uri): Pair<String, Long>? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
        if (it.moveToFirst()) {
            val name =
                if (nameIndex != -1) it.getString(nameIndex) else context.getString(R.string.file)
            val size = if (sizeIndex != -1) it.getLong(sizeIndex) else -1L
            return name to size
        }
    }
    return null
}

fun formatFileSize(size: Long): String {
    if (size <= 0) return "Bilinmiyor"
    val kb = size / 1024.0
    return when {
        kb < 1024 -> String.format("%.0f KB", kb)
        kb < 1024 * 1024 -> String.format("%.1f MB", kb / 1024)
        else -> String.format("%.1f GB", kb / (1024 * 1024))
    }
}

fun Uri.toAttachment(context: Context): Attachment {
    val (name, size) = queryFileMeta(context, this) ?: (context.getString(R.string.unknown) to -1L)

    val mimeType = context.contentResolver.getType(this) ?: "application/octet-stream"
    val type = if (mimeType.startsWith("image/")) AttachmentType.IMAGE else AttachmentType.FILE

    return Attachment(
        id = UUID.randomUUID().toString(),
        type = type,
        url = this.toString(),
        name = name,
        size = size
    )
}

val internalNoteTriple = Triple(
    yellowContainer,
    yellowOnContainer,
    yellowPrimary
)