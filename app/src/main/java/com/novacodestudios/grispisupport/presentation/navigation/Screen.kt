package com.novacodestudios.grispisupport.presentation.navigation

import com.novacodestudios.grispisupport.presentation.profile.UserTicketFilter
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object SignIn : Screen

    @Serializable
    data object List : Screen

    @Serializable
    data class Detail(val id: String) : Screen

    @Serializable
    data object Notification : Screen

    @Serializable
    data object Feedback : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data class Profile(val id: String) : Screen

    @Serializable
    data class FilteredTickets(val userId: String,val filter: UserTicketFilter ) : Screen

}