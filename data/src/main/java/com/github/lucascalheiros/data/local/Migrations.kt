package com.github.lucascalheiros.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("alter table FilterDb rename column updateAt to dateLimit")
        db.execSQL("alter table FilterDb remove column onlyChannels")
        db.execSQL("alter table FilterDb add column regex TEXT")
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("alter table FilterDb add column strategy TEXT not null default 'TelegramQuerySearch'")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("alter table FilterDb add column newMessagesCount INTEGER not null default 0")
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("alter table FilterDb rename column strategy to type")
        db.execSQL("alter table FilterDb add column fuzzyDistance INTEGER not null default 1")
    }
}

val migrations = arrayOf(
    MIGRATION_1_2,
    MIGRATION_3_4,
    MIGRATION_4_5,
    MIGRATION_5_6,
    MIGRATION_6_7
)