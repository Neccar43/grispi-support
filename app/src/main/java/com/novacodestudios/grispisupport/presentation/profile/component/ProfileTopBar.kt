package com.novacodestudios.grispisupport.presentation.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.novacodestudios.grispisupport.presentation.component.LargeProfileCircle
import com.novacodestudios.grispisupport.presentation.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopBar(navigateUp: () -> Unit, user: User) {
    CenterAlignedTopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = user.name)
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    null
                )
            }
        },
        actions = {
            LargeProfileCircle(
                name = user.name,
            )
        }
    )
}