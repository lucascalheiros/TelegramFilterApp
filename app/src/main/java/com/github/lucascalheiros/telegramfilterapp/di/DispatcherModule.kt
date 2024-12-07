package com.github.lucascalheiros.telegramfilterapp.di

import com.github.lucascalheiros.common.di.DefaultDispatcher
import com.github.lucascalheiros.common.di.IoDispatcher
import com.github.lucascalheiros.common.di.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @IoDispatcher
    @Provides
    fun providesDispatcherIO(): CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesDispatcherMain(): CoroutineDispatcher = Dispatchers.Main

    @DefaultDispatcher
    @Provides
    fun providesDispatcherDefault(): CoroutineDispatcher = Dispatchers.Default

}