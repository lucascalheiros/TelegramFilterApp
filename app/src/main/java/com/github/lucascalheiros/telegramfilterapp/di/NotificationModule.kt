package com.github.lucascalheiros.telegramfilterapp.di

import com.github.lucascalheiros.data.notification.FilteredNotificationEmitter
import com.github.lucascalheiros.telegramfilterapp.notification.FilteredNotificationEmitterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NotificationModuleBindings {

    @Binds
    fun bindFilteredNotificationEmitter(impl: FilteredNotificationEmitterImpl): FilteredNotificationEmitter

}