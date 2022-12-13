package com.example.pum_lista3.data.repositories

import android.database.Observable
import android.util.Log
import com.example.pum_lista3.domain.interfaces.TodoListInterface
import com.example.pum_lista3.data.room.databases.ToDoListDb
import com.example.pum_lista3.domain.entities.TodoList
import java.time.LocalDate
import javax.inject.Inject

class TodoListRepository @Inject constructor(
  val db: ToDoListDb
) : TodoListInterface {
  override fun getAllLists(): Observable<List<TodoList>?> {
    TODO("Not yet implemented")
  }

  override fun getListById(id: String): Observable<TodoList?> {
    TODO("Not yet implemented")
  }

  override suspend fun addList(
    listNumber: Int,
    deadline: LocalDate,
    description: String
  ) {
    Log.d("REPOSITORY", listNumber.toString())
    Log.d("REPOSITORY", deadline.toString())
    Log.d("REPOSITORY", description)
  }

  override suspend fun updateList(
    id: String,
    listNumber: Int?,
    deadline: LocalDate?,
    description: String?
  ) {
    TODO("Not yet implemented")
  }

  override suspend fun deleteList(id: String) {
    TODO("Not yet implemented")
  }
}