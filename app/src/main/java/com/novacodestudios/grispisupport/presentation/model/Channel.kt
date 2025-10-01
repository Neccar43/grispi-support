package com.novacodestudios.grispisupport.presentation.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.novacodestudios.grispisupport.R

enum class Channel {
    PUBLIC_RESPONSE,
    INTERNAL_NOTE,
    WHATSAPP,
    WEBCHAT,
    FACEBOOK_MESSENGER,
    INSTAGRAM_DM,
    INSTAGRAM_COMMENT
}

@Composable
fun Channel.toUiText(): String {
    return when (this) {
        Channel.PUBLIC_RESPONSE -> stringResource(R.string.public_response)
        Channel.INTERNAL_NOTE -> stringResource(R.string.internal_note)
        Channel.WHATSAPP -> stringResource(R.string.whatsapp)
        Channel.WEBCHAT -> stringResource(R.string.webchat)
        Channel.FACEBOOK_MESSENGER -> stringResource(R.string.facebook_messenger)
        Channel.INSTAGRAM_DM -> stringResource(R.string.instagram_dm)
        Channel.INSTAGRAM_COMMENT -> stringResource(R.string.instagram_comment)
    }
}