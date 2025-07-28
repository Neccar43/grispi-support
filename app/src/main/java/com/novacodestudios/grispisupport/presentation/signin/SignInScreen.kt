package com.novacodestudios.grispisupport.presentation.signin

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateList:()-> Unit,
) {
    val snackbarHostState =
        remember { SnackbarHostState() }

    // val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { state ->
            when (state) {
                is SignInViewModel.UIEvent.ShowSnackBar -> snackbarHostState.showSnackbar(state.message)
                SignInViewModel.UIEvent.NavigateList -> navigateList()
            }
        }
    }
    SignInScreenContent(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreenContent(
    state: SignInState,
    snackbarHostState: SnackbarHostState,
    onEvent: (SignInEvent) -> Unit,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {})
                    { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                title = {
                    Icon(
                        painter = painterResource(com.novacodestudios.grispisupport.R.drawable.logo),
                        tint = Color(0xFF632D91),
                       contentDescription =  null
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .imePadding() ,
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.domain,
                onValueChange = { onEvent(SignInEvent.OnDomainChange(it)) },
                placeholder = { Text("alt alan") },
                suffix = {
                    Text(
                        text = ".grispi.com",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                supportingText = {Text(text = state.domainError?:"Grispi Support'ta oturum açmak için kullandığınız adres budur.")},
                isError = state.domainError!=null,
                singleLine = true
                //colors = TextFieldDefaults.colors(unfocusedContainerColor = Color.Transparent, focusedContainerColor = Color.Transparent, errorContainerColor = Color.Transparent)
            )
            Spacer(modifier = Modifier.height(300.dp))
            Button (
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(SignInEvent.OnNextClick) },
                enabled = state.domain.isNotBlank()
            ){
                Text("Sonraki")
            }

            TextButton(onClick = {}) {
                Text("GİZLİLİK POLİTİKASI")
            }
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    GrispiSupportTheme {
        SignInScreenContent(
            state = SignInState(),
            snackbarHostState = SnackbarHostState(),
            onEvent = {}
        )
    }
}
