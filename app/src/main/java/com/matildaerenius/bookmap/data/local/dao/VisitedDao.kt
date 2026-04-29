package com.matildaerenius.bookmap.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.matildaerenius.bookmap.data.local.entity.VisitedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitedDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisited(visited: VisitedEntity)

    @Query("DELETE FROM visited_entity WHERE bookId = :bookId")
    suspend fun deleteVisited(bookId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM visited_entity WHERE bookId = :bookId)")
    fun observeIsVisited(bookId: Int): Flow<Boolean>

    @Query("SELECT * FROM visited_entity")
    fun getAllVisited(): Flow<List<VisitedEntity>>
}