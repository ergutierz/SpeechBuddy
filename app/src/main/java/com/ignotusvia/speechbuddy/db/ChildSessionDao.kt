package com.ignotusvia.speechbuddy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChildSessionDao {
    @Insert
    suspend fun insert(session: ChildSession)

    @Query("SELECT * FROM child_sessions")
    suspend fun getAll(): List<ChildSession>
}
