package com.github.lucascalheiros.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FilterDb(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val title: String,
    val limitDate: Long,
    val regex: String,
    val strategy: FilterStrategyDb
)