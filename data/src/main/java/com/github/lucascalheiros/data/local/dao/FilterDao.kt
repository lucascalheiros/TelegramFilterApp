package com.github.lucascalheiros.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.lucascalheiros.data.model.ChatToFilterInfoCrossRefDb
import com.github.lucascalheiros.data.model.FilterDb
import com.github.lucascalheiros.data.model.FilterToQueriesCrossRefDb
import com.github.lucascalheiros.data.model.FilterWithQueriesAndChats

@Dao
interface FilterDao {

    @Query("select * from FilterDb")
    suspend fun getFilters(): List<FilterWithQueriesAndChats>

    @Query("select * from FilterDb where id = :id")
    suspend fun getFilter(id: Long): FilterWithQueriesAndChats?

    @Query("delete from ChatToFilterInfoCrossRefDb where filterId = :id")
    suspend fun clearChatIds(id: Long)

    @Query("delete from FilterToQueriesCrossRefDb where filterId = :id")
    suspend fun clearQueries(id: Long)

    @Query("delete from FilterDb where id = :id")
    suspend fun clearFilter(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChats(data: List<ChatToFilterInfoCrossRefDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQueries(data: List<FilterToQueriesCrossRefDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(data: FilterDb): Long

    @Transaction
    suspend fun save(
        filterDb: FilterDb,
        chatIds: List<Long>,
        queries: List<String>
    ) {
        val id = save(filterDb)
        clearChatIds(id)
        clearQueries(id)
        saveChats(chatIds.map { ChatToFilterInfoCrossRefDb(it, id) })
        saveQueries(queries.map { FilterToQueriesCrossRefDb(id, it) })
    }

    @Transaction
    suspend fun deleteFilter(id: Long) {
        clearFilter(id)
        clearQueries(id)
        clearChatIds(id)
    }

}