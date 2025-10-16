package com.novacodestudios.grispisupport.presentation.feedback

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.R
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is FeedbackViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(state.message)
            }
        }
    }
    FeedbackScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        navigateUp = navigateUp
    )
}

@Composable
fun FeedbackScreenContent(
    state: FeedbackState,
    snackbarHostState: SnackbarHostState,
    onEvent: (FeedbackEvent) -> Unit,
    navigateUp: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { FeedbackTopBar(navigateUp = navigateUp) }
    ) { paddingValues ->
        var selectedStars by remember { mutableIntStateOf(0) }
        var comment by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(128.dp))
            Row {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= selectedStars) Icons.Default.Star else Icons.Default.StarOutline,
                        contentDescription = "$i yıldız",
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { selectedStars = i },
                        tint = Color(0xFFFFD700) // Sarı renk
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Yorumunuz") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )
        }
    }
}

@Preview
@Composable
private fun FeedBackPrev() {
    GrispiSupportTheme {
        FeedbackScreenContent(
            state = FeedbackState(),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
            navigateUp = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackTopBar(navigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.feedback)) },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    null
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    Icons.AutoMirrored.Default.Send,
                    null
                )
            }
        }
    )
}
