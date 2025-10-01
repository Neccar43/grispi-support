package com.novacodestudios.grispisupport.presentation.signin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.novacodestudios.grispisupport.R
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateList: () -> Unit,
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
                        contentDescription = null
                    )
                }
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(NavigationBarDefaults.windowInsets)
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            AnimatedVisibility(
                visible = state.isDomainValid,
                enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.email,
                        onValueChange = { onEvent(SignInEvent.OnEmailChange(it)) },
                        placeholder = { Text(stringResource(id = R.string.signin_email_placeholder)) },
                        //supportingText = {Text(text = state.domainError)},
                        // isError = state.domainError!=null,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),

                        )
                    var passwordVisible by remember { mutableStateOf(false) }
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.password,
                        onValueChange = { onEvent(SignInEvent.OnPasswordChange(it)) },
                        placeholder = { Text(stringResource(id = R.string.signin_password_placeholder)) },
//                    supportingText = {Text(text = state.domainError?:"Grispi Support'ta oturum açmak için kullandığınız adres budur.")},
//                    isError = state.domainError!=null,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            errorContainerColor = Color.Transparent
                        ),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                    )
                    TextButton(
                        onClick = {}
                    ) {
                        Text(stringResource(id = R.string.signin_forgot_password))
                    }
                }
            }
            if (!state.isDomainValid) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.domain,
                    onValueChange = { onEvent(SignInEvent.OnDomainChange(it)) },
                    placeholder = { Text(stringResource(id = R.string.signin_subdomain_placeholder)) },
                    suffix = {
                        Text(
                            text = stringResource(id = R.string.signin_domain_suffix),
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    supportingText = {
                        Text(
                            text = state.domainError
                                ?: stringResource(id = R.string.signin_domain_helper)
                        )
                    },
                    isError = state.domainError != null,
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(SignInEvent.OnNextClick) },
                enabled = state.domain.isNotBlank()
            ) {
                Text(stringResource(id = R.string.signin_next_button))
            }

            TextButton(
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.ime
                        .union(NavigationBarDefaults.windowInsets)
                        .only(WindowInsetsSides.Bottom)
                ),
                onClick = {}) {
                Text(stringResource(id = R.string.signin_privacy_policy))
            }
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    GrispiSupportTheme {
        SignInScreenContent(
            state = SignInState(isDomainValid = true),
            snackbarHostState = SnackbarHostState(),
            onEvent = {}
        )
    }
}
