package com.example.pum_lista3.data.room.accessObjects

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pum_lista3.data.room.entities.ToDoList

@Dao
interface ToDoListDao {
  @Query("SELECT * FROM toDoList")
  fun getAll(): List<ToDoList>

  @Query("SELECT * FROM toDoList WHERE uid=:id")
  fun getById(id: String)

  @Insert
  fun insertToDoList(toDoList: ToDoList)

  @Update
  fun updateToDoList(toDoList: ToDoList)

  @Delete
  fun deleteToDoList(toDoList: ToDoList)
}