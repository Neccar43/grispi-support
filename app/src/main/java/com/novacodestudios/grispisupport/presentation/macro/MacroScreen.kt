package com.novacodestudios.grispisupport.presentation.macro

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.R
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MacroScreen(
    viewModel: MacroViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateDetail: (id: String, Macro) -> Unit,
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is MacroViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(state.message)
            }
        }
    }
    MacroScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        navigateUp = navigateUp,
        navigateDetail = navigateDetail
    )
}

@Composable
fun MacroScreenContent(
    state: MacroState,
    snackbarHostState: SnackbarHostState,
    onEvent: (MacroEvent) -> Unit,
    navigateUp: () -> Unit,
    navigateDetail: (ticketId: String, Macro) -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { MacroTopBar(navigateUp = navigateUp) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(items = state.macros, key = { it.id }) {
                ListItem(
                    modifier = Modifier.clickable { navigateDetail(state.ticketId, it) },
                    leadingContent = {
                        Icon(
                            Icons.Default.Bolt,
                            null
                        )
                    },
                    headlineContent = { Text(it.title) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun MSP() {
    GrispiSupportTheme {
        MacroScreenContent(
            state = MacroState(
                // macros = dummyMacros,
                ticketId = "t1"
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onEvent = {},
            navigateUp = {},
            navigateDetail = { _, _ -> }
        )
    }
}


data class Macro(
    val id: String,
    val title: String,
    val actions: List<MacroAction>
)

data class MacroAction(
    val field: String,
    val value: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MacroTopBar(navigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.macros)) },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    null
                )
            }
        },
    )
}