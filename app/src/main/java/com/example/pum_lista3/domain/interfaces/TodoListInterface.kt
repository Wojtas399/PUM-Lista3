package com.example.pum_lista3.domain.interfaces

import android.database.Observable
import com.example.pum_lista3.domain.entities.TodoList
import java.time.LocalDate

interface TodoListInterface {
  fun getAllLists(): Observable<List<TodoList>?>

  fun getListById(id: String): Observable<TodoList?>

  suspend fun addList(
    listNumber: Int,
    deadline: LocalDate,
    description: String,
  )

  suspend fun updateList(
    id: String,
    listNumber: Int?,
    deadline: LocalDate?,
    description: String?
  )

  suspend fun deleteList(id: String)
}
