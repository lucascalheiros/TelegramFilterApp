package com.github.lucascalheiros.telegramfilterapp.notification.channels

import com.github.lucascalheiros.domain.model.Filter

sealed interface ChannelType {
    val channelId: String
    val title: String
    data class FilteredMessage(val filter: Filter): ChannelType {
        override val title: String = filter.title
        override val channelId: String = channelIdPrefix(filter.id) + filter.title

        companion object {

            private const val prefix: String = "FilteredMessage"

            fun channelIdPrefix(filterId: Long): String {
                return "$prefix$filterId-"
            }
        }
    }
}