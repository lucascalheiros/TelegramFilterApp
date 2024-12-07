package com.github.lucascalheiros.data.model

import androidx.room.Entity

@Entity(primaryKeys = ["chatId", "filterId"])
data class ChatToFilterInfoCrossRefDb(
    val chatId: Long,
    val filterId: Long
)