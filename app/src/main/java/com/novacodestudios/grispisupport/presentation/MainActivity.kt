package com.novacodestudios.grispisupport.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.novacodestudios.grispisupport.R
import com.novacodestudios.grispisupport.data.local.Keys
import com.novacodestudios.grispisupport.data.local.Preferences
import com.novacodestudios.grispisupport.presentation.navigation.GrispiDrawer
import com.novacodestudios.grispisupport.presentation.navigation.NavigationItem
import com.novacodestudios.grispisupport.presentation.navigation.Screen
import com.novacodestudios.grispisupport.presentation.settings.LanguageOption
import com.novacodestudios.grispisupport.presentation.settings.SettingsViewModel
import com.novacodestudios.grispisupport.presentation.settings.setAppLanguageForLegacy
import com.novacodestudios.grispisupport.presentation.settings.toLanguageCode
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainActivity"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var preferences: Preferences
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val langCode=preferences.getData(Keys.LANGUAGE)?.let { LanguageOption.valueOf(it).toLanguageCode() } ?: return@launch
            setAppLanguageForLegacy(this@MainActivity, langCode)
            Log.d(TAG, "onCreate: Language code: $langCode")
        }

        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()

            GrispiSupportTheme(
                darkTheme = settingsViewModel.state.theme
            ) {
                val appState = rememberAppState()
                GrispiDrawer(modifier = Modifier, appState = appState)
            }
        }
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
): AppState = remember {
    AppState(
        navController,
        coroutineScope,
        drawerState
    )
}


@Stable
class AppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val drawerState: DrawerState
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    @Composable
    fun isSelected(item: NavigationItem) = currentDestination?.hasRoute(item.route::class) == true

    val navItems @Composable get() = listOf(
        NavigationItem(
            title = stringResource(R.string.notifications),
            selectedIcon = Icons.Filled.Notifications,
            unSelectedIcon = Icons.Outlined.Notifications,
            route = Screen.Notification
        ),
        NavigationItem(
            title = stringResource(R.string.feedback),
            selectedIcon = Icons.Filled.Feedback,
            unSelectedIcon = Icons.Outlined.Feedback,
            route = Screen.Feedback
        ),
        NavigationItem(
            title = stringResource(R.string.settings),
            selectedIcon = Icons.Filled.Settings,
            unSelectedIcon = Icons.Outlined.Settings,
            route = Screen.Settings
        ),

        )

    val currentUser = com.novacodestudios.grispisupport.presentation.util.currentUser

}