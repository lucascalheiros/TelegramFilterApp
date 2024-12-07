package com.github.lucascalheiros.data.model

import androidx.room.Entity

@Entity(primaryKeys = ["filterId", "query"])
data class FilterToQueriesCrossRefDb(
    val filterId: Long,
    val query: String
)