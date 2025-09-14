package com.novacodestudios.grispisupport.presentation.detail.component

import android.Manifest
import android.R
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.PhotoCamera
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil3.compose.rememberAsyncImagePainter
import com.novacodestudios.grispisupport.presentation.detail.DetailEvent
import com.novacodestudios.grispisupport.presentation.detail.DetailState
import com.novacodestudios.grispisupport.presentation.detail.DetailTabs
import com.novacodestudios.grispisupport.presentation.model.Channel
import com.novacodestudios.grispisupport.presentation.model.TicketStatus
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.util.dummyTicketList
import com.novacodestudios.grispisupport.presentation.util.toColor
import com.novacodestudios.grispisupport.presentation.util.toUiName
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ReplyCard(
    modifier: Modifier = Modifier,
    state: DetailState,
    onEvent: (DetailEvent) -> Unit,
    navigateMacro: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false ) }
    val focusRequester = remember { FocusRequester() }

    val outerPadding = remember(isFocused) { if (isFocused) 16.dp else 0.dp }
    val ticket = state.ticket ?: return

    var capturedUris by remember { mutableStateOf(listOf<Uri>()) }

    val openCamera = rememberCameraLauncher { uri ->
        uri?.let {
            capturedUris = capturedUris + it // listeye ekle
        }
    }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            focusRequester.requestFocus()
            onEvent(DetailEvent.OnActiveTabChange(DetailTabs.Conversation))
        }
    }
    ElevatedCard(
        modifier = modifier,
        shape = RectangleShape
    ) {
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
            var option by remember { mutableStateOf(Channel.PUBLIC_RESPONSE) }
            if (isFocused) {
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Şu yolla yanıtla: ", style = MaterialTheme.typography.bodyMedium)
                        Row(
                            modifier = Modifier
                                .clickable { expanded = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = option.title,
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                            )

                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf(
                            Channel.PUBLIC_RESPONSE,
                            Channel.INTERNAL_NOTE
                        ).forEach { newOption ->
                            DropdownMenuItem(
                                text = { Text(newOption.title) },
                                onClick = { option = newOption;expanded = false }
                            )
                        }
                    }
                }
                StdBasicTextField(
                    value = state.replyText,
                    onValueChange = { onEvent(DetailEvent.OnReplyTextChange(it)) },
                    placeholder = "Yanıt yazın...",
                    modifier = Modifier.focusRequester(focusRequester)
                )
                if (capturedUris.isNotEmpty()){
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(capturedUris) { uri ->
                            CapturedImageWithCloseButton(
                                imageUri = uri,
                                onClose = { capturedUris = capturedUris - uri },
                                onClickImage = {  }
                            )
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
                    // TODO: dosyalar açılacak
                }) {
                    Icon(Icons.Default.Attachment, null)
                }
                IconButton(onClick = {
                    // TODO: mention menu açılacak
                    // TODO: @Kişi kısmı primary color olacak
                    onEvent(DetailEvent.OnReplyTextChange(state.replyText + " @"))
                }) {
                    Icon(Icons.Default.AlternateEmail, null)
                }
            }
            if (!isFocused) {
                StdBasicTextField(
                    value = state.replyText,
                    onValueChange = { onEvent(DetailEvent.OnReplyTextChange(it)) },
                    placeholder = "Yanıt yazın...",
                    modifier = Modifier.onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            isFocused = true
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            var isExpanded by remember { mutableStateOf(false) }
            Box {
                Row(
                    modifier = Modifier
                        .clickable { isExpanded = true }
                        .background(
                            color = CardDefaults.cardColors().containerColor,
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
                    onDismissRequest = { isExpanded = false }
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
                        onClick = {},
                        enabled = state.replyText.isNotBlank(),
                        // colors = IconButtonDefaults.iconButtonColors().copy(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun RCP() {
    GrispiSupportTheme {
        ReplyCard(
            state = DetailState(
                ticket = dummyTicketList.first(),
                replyText = "Merhaba, size nasıl yardımcı olabilirim?"
            ),
            onEvent = {},
            navigateMacro = {}
        )
    }

}

@Composable
fun rememberCameraLauncher(
    onImageCaptured: (Uri?) -> Unit
): () -> Unit {
    val context = LocalContext.current

    // Runtime permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                Toast.makeText(context, "Kamera izni gerekli", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Fotoğraf kaydedilecek geçici dosya
    val imageFile = remember {
        File(context.cacheDir, "captured_image.jpg")
    }
    val imageUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        imageFile
    )

    // Kamera açıcı launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) onImageCaptured(imageUri) else onImageCaptured(null)
        }
    )

    // Dışarıya dönecek fonksiyon
    return {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // izin varsa → kamera aç
            cameraLauncher.launch(imageUri)
        } else {
            // izin yoksa → izin iste
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

@Composable
fun CapturedImageWithCloseButton(
    imageUri: Uri,
    onClose: () -> Unit,
    onClickImage: () -> Unit
) {
    BadgedBox(
        modifier = Modifier
           .height(50.dp),
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
