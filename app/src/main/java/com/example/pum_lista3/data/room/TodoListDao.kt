package com.example.pum_lista3.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {
  @Query("SELECT * FROM todoListDbModel")
  fun getAll(): Flow<List<TodoListDbModel>>

  @Query("SELECT * FROM todoListDbModel WHERE entryId=:id")
  fun getById(id: String): Flow<TodoListDbModel>

  @Insert
  fun insertToDoList(toDoList: TodoListDbModel)

  @Update
  fun updateToDoList(toDoList: TodoListDbModel)

  @Query("DELETE FROM todoListDbModel WHERE entryId=:id")
  fun deleteToDoListById(id: String)
}