package com.novacodestudios.grispisupport.presentation.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.presentation.detail.component.ConversationSection
import com.novacodestudios.grispisupport.presentation.detail.component.DetailSection
import com.novacodestudios.grispisupport.presentation.detail.component.DetailTopBar
import com.novacodestudios.grispisupport.presentation.detail.component.ExtensionSection
import com.novacodestudios.grispisupport.presentation.detail.component.HistorySection
import com.novacodestudios.grispisupport.presentation.detail.component.ReplyCard
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import com.novacodestudios.grispisupport.presentation.util.dummyHistories
import com.novacodestudios.grispisupport.presentation.util.dummyTicketList
import com.novacodestudios.grispisupport.presentation.util.messagesT1
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateProfile: (String) -> Unit,
    navigateMacro: (String) -> Unit,
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is DetailViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(state.message)
            }
        }
    }
    DetailScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        navigateUp = navigateUp,
        navigateProfile = navigateProfile,
        navigateMacro = navigateMacro
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    state: DetailState,
    snackbarHostState: SnackbarHostState,
    onEvent: (DetailEvent) -> Unit,
    navigateUp: () -> Unit,
    navigateProfile: (String) -> Unit,
    navigateMacro: (String) -> Unit,
) {
    if (state.ticket == null) {
        // TODO: Handle et
        return
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DetailTopBar(
                state.ticket,
                navigateUp = navigateUp,
                onTitleClick = navigateProfile
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(NavigationBarDefaults.windowInsets)
    )
    { paddingValues ->
        val pagerState = rememberPagerState(
            initialPage = DetailTabs.entries.indexOf(state.activeTab),
            pageCount = { DetailTabs.entries.size }
        )
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                DetailTabs.entries.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                                onEvent(DetailEvent.OnActiveTabChange(tab))
                            }
                        },
                        text = { Text(tab.title) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (DetailTabs.entries[page]) {
                    DetailTabs.Conversation -> ConversationSection(
                        ticket = state.ticket,
                        messageList = state.messageList,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 8.dp)
                    )

                    DetailTabs.Detail -> DetailSection(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 8.dp),
                        // .verticalScroll(rememberScrollState()),
                        state = state,
                        onEvent = onEvent
                    )

                    DetailTabs.History -> HistorySection(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                            .padding(horizontal = 16.dp),
                        ticketHistories = state.ticketHistories
                    )

                    DetailTabs.Extension -> ExtensionSection(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
            ReplyCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(
                        WindowInsets.ime
                            .union(NavigationBarDefaults.windowInsets)
                            .only(WindowInsetsSides.Bottom)
                    ),
                state = state,
                onEvent = onEvent,
                navigateMacro = navigateMacro
            )
        }
    }
}

@Preview
@Composable
private fun DetailScreenPreview() {
    GrispiSupportTheme {
        DetailScreenContent(
            state = DetailState(
                ticket = dummyTicketList.first(),
                activeTab = DetailTabs.Conversation,
                messageList = messagesT1.sortedBy { it.sentAt },
                ticketHistories = dummyHistories.filter { it.ticketId == "t1" }
                    .sortedByDescending { it.createdAt }

            ),
            snackbarHostState = SnackbarHostState(),
            onEvent = {},
            navigateUp = {},
            navigateProfile = {},
            navigateMacro = {}
        )
    }
}

enum class DetailTabs(val title: String) {
    Conversation("Sohbet"),
    Detail("Detay"),
    Extension("Uygulama"),
    History("Geçmiş")
}






