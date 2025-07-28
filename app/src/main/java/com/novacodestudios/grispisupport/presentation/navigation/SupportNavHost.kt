package com.novacodestudios.grispisupport.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.novacodestudios.grispisupport.presentation.detail.DetailScreen
import com.novacodestudios.grispisupport.presentation.list.ListScreen
import com.novacodestudios.grispisupport.presentation.signin.SignInScreen

@Composable
fun SupportNavHost(modifier: Modifier = Modifier,navController: NavHostController) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.SignIn,
        enterTransition = enterTransition(),
        exitTransition = exitTransition(),
        popEnterTransition = popEnterTransition(),
        popExitTransition = popExitTransition()
    ){
        composable<Screen.SignIn> {
            SignInScreen(navigateList = {navController.navigate(Screen.List){
                popUpTo(Screen.SignIn){
                    inclusive=true
                }
            } })
        }
        composable<Screen.List> {
            ListScreen(navigateDetail = {navController.navigate(Screen.Detail(it))})
        }
        composable<Screen.Detail> {
            DetailScreen(navigateUp = {navController.navigateUp()})
        }
    }
}