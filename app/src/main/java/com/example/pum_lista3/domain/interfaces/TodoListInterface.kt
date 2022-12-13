package com.example.pum_lista3.domain.interfaces

import com.example.pum_lista3.domain.entities.TodoList
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TodoListInterface {
  fun getAllLists(): Flow<List<TodoList>>

  fun getListById(id: String): Flow<TodoList>

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
