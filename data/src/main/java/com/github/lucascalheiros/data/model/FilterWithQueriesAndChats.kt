package com.github.lucascalheiros.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class FilterWithQueriesAndChats(
    @Embedded val filterDb: FilterDb,
    @Relation(parentColumn = "id", entityColumn = "filterId")
    val queries: List<FilterToQueriesCrossRefDb>,
    @Relation(parentColumn = "id", entityColumn = "filterId")
    val chats: List<ChatToFilterInfoCrossRefDb>
)