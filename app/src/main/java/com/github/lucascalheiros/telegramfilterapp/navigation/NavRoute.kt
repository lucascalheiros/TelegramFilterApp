package com.github.lucascalheiros.telegramfilterapp.navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {

    @Serializable
    data object Redirect : NavRoute

    @Serializable
    data object Setup : NavRoute

    @Serializable
    data object FilterList : NavRoute

    @Serializable
    data class FilterSettings(val filterId: Long? = null) : NavRoute

    @Serializable
    data class FilterMessages(val filterId: Long? = null) : NavRoute
}