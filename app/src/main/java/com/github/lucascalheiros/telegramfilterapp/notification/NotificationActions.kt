package com.github.lucascalheiros.telegramfilterapp.notification

import android.content.Intent
import android.os.Bundle

sealed interface NotificationActions {

    data class OpenFilterMessages(val filterId: Long, val newMessageId: Long): NotificationActions {

        fun extras(): Bundle {
            return Bundle().apply {
                putLong(OPEN_FILTER_MESSAGES_EXTRA_KEY, filterId)
                putLong(OPEN_FILTER_MESSAGES_NEW_MESSAGE_EXTRA_KEY, newMessageId)
            }
        }

        fun actionIdentifier(): String {
            return hashCode().toString()
        }

        companion object {
            private const val OPEN_FILTER_MESSAGES_EXTRA_KEY: String = "OPEN_FILTER_MESSAGES_EXTRA_KEY"
            private const val OPEN_FILTER_MESSAGES_NEW_MESSAGE_EXTRA_KEY: String = "OPEN_FILTER_MESSAGES_NEW_MESSAGE_EXTRA_KEY"

            fun extract(intent: Intent): OpenFilterMessages? {
                val filterId = intent.getLongExtra(OPEN_FILTER_MESSAGES_EXTRA_KEY, -1)
                val messageId = intent.getLongExtra(OPEN_FILTER_MESSAGES_NEW_MESSAGE_EXTRA_KEY, -1)
                if (filterId == -1L) {
                    return null
                }
                return OpenFilterMessages(filterId, messageId)
            }
        }
    }
}