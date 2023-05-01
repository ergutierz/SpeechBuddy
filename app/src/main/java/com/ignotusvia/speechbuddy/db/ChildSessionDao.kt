package com.ignotusvia.speechbuddy.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChildSessionDao {
    @Insert
    fun insert(childSessionEntity: ChildSessionEntity): Long

    @Query("SELECT * FROM child_sessions")
    fun getAll(): List<ChildSessionEntity>

    @Query("SELECT * FROM child_sessions WHERE id = :id")
    fun getById(id: Int): ChildSessionEntity?

    @Update
    fun update(childSessionEntity: ChildSessionEntity)

    @Delete
    fun delete(childSessionEntity: ChildSessionEntity)
}
