package com.github.lucascalheiros.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.lucascalheiros.data.local.dao.FilterDao
import com.github.lucascalheiros.data.model.ChatToFilterInfoCrossRefDb
import com.github.lucascalheiros.data.model.FilterDb
import com.github.lucascalheiros.data.model.FilterToQueriesCrossRefDb

@Database(
    entities = [
        FilterDb::class,
        ChatToFilterInfoCrossRefDb::class,
        FilterToQueriesCrossRefDb::class,
    ],
    exportSchema = true,
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filterDao(): FilterDao
}
