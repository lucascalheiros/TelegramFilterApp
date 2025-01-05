package com.github.lucascalheiros.data.di

import android.content.Context
import androidx.room.Room
import com.github.lucascalheiros.data.local.AppDatabase
import com.github.lucascalheiros.data.local.dao.FilterDao
import com.github.lucascalheiros.data.local.migrations
import com.github.lucascalheiros.data.repositories.ChatRepositoryImpl
import com.github.lucascalheiros.data.repositories.FilterRepositoryImpl
import com.github.lucascalheiros.data.repositories.MessageRepositoryImpl
import com.github.lucascalheiros.data.repositories.TelegramSetupRepositoryImpl
import com.github.lucascalheiros.domain.repositories.ChatRepository
import com.github.lucascalheiros.domain.repositories.FilterRepository
import com.github.lucascalheiros.domain.repositories.MessageRepository
import com.github.lucascalheiros.domain.repositories.TelegramSetupRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-database"
        ).addMigrations(
            *migrations
        ).build()
    }

    @Provides
    fun provideFilterDao(db: AppDatabase): FilterDao {
        return db.filterDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface FilterRepositoryBinding {
    @Binds
    fun bindFilterRepository(impl: FilterRepositoryImpl): FilterRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface MessageRepositoryBinding {
    @Binds
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface TelegramSetupRepositoryBinding {
    @Binds
    fun bindTelegramSetupRepository(impl: TelegramSetupRepositoryImpl): TelegramSetupRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface ChatRepositoryBinding {
    @Binds
    fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
}
