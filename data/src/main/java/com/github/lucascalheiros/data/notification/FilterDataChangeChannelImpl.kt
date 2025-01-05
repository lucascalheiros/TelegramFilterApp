package com.github.lucascalheiros.data.notification

import com.github.lucascalheiros.common.di.IoDispatcher
import com.github.lucascalheiros.domain.notifications.FilterDataChangeChannel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterDataChangeChannelImpl @Inject constructor(
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
): FilterDataChangeChannel {
    override val channel: Channel<Unit> = Channel(Channel.CONFLATED)

    fun emit() = CoroutineScope(coroutineDispatcher).launch {
        channel.send(Unit)
    }
}