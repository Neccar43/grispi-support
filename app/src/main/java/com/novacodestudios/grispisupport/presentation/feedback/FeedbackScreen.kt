package com.novacodestudios.grispisupport.presentation.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.R
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Geri Bildirim Ekranı - Henüz Yapılmadı",
                style = MaterialTheme.typography.headlineMedium
            )
        }
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
