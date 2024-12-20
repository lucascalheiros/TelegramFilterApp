package com.github.lucascalheiros.telegramfilterapp.notification.channels

import com.github.lucascalheiros.domain.model.Filter

sealed interface ChannelType {
    val channelId: String
    val prefix: String
    val title: String
    data class FilteredMessage(val filter: Filter): ChannelType {
        override val prefix: String = "FilteredMessage"
        override val title: String = filter.title
        override val channelId: String = "$prefix${filter.id}"
    }
}