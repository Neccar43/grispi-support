package com.novacodestudios.grispisupport.presentation.model

data class Application(
    val id: String,
    val iconUrl: String,
    val rating: Float,
    val commentsCount: Int,
    val price: String,
    val name: String,
    val description: String,

    )