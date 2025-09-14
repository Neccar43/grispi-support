package com.novacodestudios.grispisupport.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.novacodestudios.grispisupport.presentation.AppState
import com.novacodestudios.grispisupport.presentation.detail.DetailScreen
import com.novacodestudios.grispisupport.presentation.feedback.FeedbackScreen
import com.novacodestudios.grispisupport.presentation.filteredtickets.FilteredTicketsScreen
import com.novacodestudios.grispisupport.presentation.list.ListScreen
import com.novacodestudios.grispisupport.presentation.notification.NotificationScreen
import com.novacodestudios.grispisupport.presentation.profile.ProfileScreen
import com.novacodestudios.grispisupport.presentation.settings.SettingsScreen
import com.novacodestudios.grispisupport.presentation.signin.SignInScreen
import kotlinx.coroutines.launch

@Composable
fun SupportNavHost(modifier: Modifier = Modifier, appState: AppState) {
    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = Screen.SignIn,
        enterTransition = enterTransition(),
        exitTransition = exitTransition(),
        popEnterTransition = popEnterTransition(),
        popExitTransition = popExitTransition()
    ) {
        val navigateUp = { appState.navController.navigateUp();Unit }
        composable<Screen.SignIn> {
            SignInScreen(navigateList = {
                appState.navController.navigate(Screen.List) {
                    popUpTo(Screen.SignIn) {
                        inclusive = true
                    }
                }
            })
        }
        composable<Screen.List> {
            ListScreen(
                onMenuClick = {
                    appState.coroutineScope.launch {
                        appState.drawerState.open()
                    }
                },
                navigateDetail = { appState.navController.navigate(Screen.Detail(it)) })
        }
        composable<Screen.Detail> {
            DetailScreen(
                navigateUp = navigateUp,
                navigateProfile = {
                    appState.navController.navigate(Screen.Profile(it))
                },
                navigateMacro = {
                    appState.navController.navigate(Screen.Macro(ticketId = it))
                }
            )
        }

        composable<Screen.Notification> {
            NotificationScreen(navigateUp = navigateUp, navigateDetail = {
                appState.navController.navigate(Screen.Detail(it))
            })
        }

        composable<Screen.Feedback> {
            FeedbackScreen(navigateUp = navigateUp)

        }
        composable<Screen.Settings> {
            SettingsScreen(
                navigateUp = navigateUp,
                navigateProfile = {
                    appState.navController.navigate(Screen.Profile(appState.currentUser.id))
                },
                navigateSignIn = {
                    appState.navController.navigate(Screen.SignIn) {
                        popUpTo(Screen.List) {
                            inclusive = true
                        }
                    }
                }
            )

        }
        composable<Screen.Profile> {
            ProfileScreen(navigateUp = navigateUp, navigateFilteredTickets = {  userId, filter ->
                appState.navController.navigate(Screen.FilteredTickets(userId, filter))
            })
        }

        composable<Screen.FilteredTickets> {
            FilteredTicketsScreen(
                navigateUp = navigateUp,
                navigateDetail = { appState.navController.navigate(Screen.Detail(it)) }
            )
        }
    }
}
