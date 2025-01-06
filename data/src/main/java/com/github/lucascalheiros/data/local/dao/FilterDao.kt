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
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterDao {

    @Query("select * from FilterDb")
    @Transaction
    fun getFilterWithQueriesAndChats(): Flow<List<FilterWithQueriesAndChats>>

    @Query("select * from FilterDb where id = :id")
    @Transaction
    suspend fun getFilterWithQueriesAndChats(id: Long): FilterWithQueriesAndChats?

    @Query("select * from FilterDb where id = :id")
    @Transaction
    suspend fun getFilter(id: Long): FilterDb?

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
    ): Long {
        val id = save(filterDb)
        clearChatIds(id)
        clearQueries(id)
        saveChats(chatIds.map { ChatToFilterInfoCrossRefDb(it, id) })
        saveQueries(queries.map { FilterToQueriesCrossRefDb(id, it) })
        return id
    }

    @Transaction
    suspend fun deleteFilter(id: Long) {
        clearFilter(id)
        clearQueries(id)
        clearChatIds(id)
    }

    @Query("UPDATE FilterDb SET newMessagesCount = :newMessagesCount WHERE id = :id")
    suspend fun setNewMessageCount(id: Long, newMessagesCount: Int)

    @Transaction
    suspend fun incrementNewMessageCounter(id: Long) {
        getFilter(id)?.also {
            setNewMessageCount(id, it.newMessagesCount + 1)
        }
    }

    @Transaction
    suspend fun resetNewMessageCounter(id: Long) {
        getFilter(id)?.also {
            setNewMessageCount(id, 0)
        }
    }
}