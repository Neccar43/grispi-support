package com.novacodestudios.grispisupport.presentation.navigation

import kotlinx.serialization.Serializable

// TODO: Search Notification Settings ekranları da implement edilecek
@Serializable
sealed interface Screen {
    @Serializable
    data object SignIn : Screen

    @Serializable
    data object List : Screen

    @Serializable
    data class Detail(val id: String) : Screen

}