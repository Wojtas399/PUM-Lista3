package com.example.pum_lista3.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomTodoListDao {
  @Query("SELECT * FROM roomTodolist")
  fun getAll(): Flow<List<RoomTodoList>>

  @Query("SELECT * FROM roomTodolist WHERE entryId=:id")
  fun getById(id: String): Flow<RoomTodoList>

  @Insert
  fun insertToDoList(toDoList: RoomTodoList)

  @Update
  fun updateToDoList(toDoList: RoomTodoList)

  @Query("DELETE FROM roomTodolist WHERE entryId=:id")
  fun deleteToDoListById(id: String)
}