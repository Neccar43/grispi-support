package com.novacodestudios.grispisupport.presentation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import com.novacodestudios.grispisupport.presentation.AppState
import com.novacodestudios.grispisupport.presentation.component.ProfileCircle
import com.novacodestudios.grispisupport.presentation.model.User
import kotlinx.coroutines.launch


@Composable
fun GrispiDrawer(modifier: Modifier = Modifier, appState: AppState) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(275.dp)
            ) {
                ProfileSection(
                    user = appState.currentUser,
                    onClick = {
                        appState.navController.navigate(Screen.Profile(appState.currentUser.id))
                        appState.coroutineScope.launch { appState.drawerState.close() }
                    }
                )
                HorizontalDivider()
                appState.navItems.forEach { item ->
                    val isSelected = appState.isSelected(item)
                    if (item.route == Screen.Settings) {
                        HorizontalDivider()
                    }

                    NavigationDrawerItem(
                        label = { Text(item.title) },
                        selected = isSelected,
                        onClick = {
                            appState.navController.navigate(item.route)
                            appState.coroutineScope.launch { appState.drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unSelectedIcon,
                                null
                            )
                        }
                    )
                }
            }
        },
        drawerState = appState.drawerState,
        gesturesEnabled = appState.currentDestination?.hasRoute(Screen.SignIn::class) == false,
    ) {
        SupportNavHost(
            modifier = modifier,
            appState = appState
        )
    }
}

@Composable
private fun ProfileSection(
    user: User,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileCircle(
            name = user.name,
            size = 48.dp,
        )
        Text(
            text = user.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
