package com.github.lucascalheiros.domain.notifications

import kotlinx.coroutines.channels.ReceiveChannel

interface FilterDataChangeChannel {
    val channel: ReceiveChannel<Unit>
}