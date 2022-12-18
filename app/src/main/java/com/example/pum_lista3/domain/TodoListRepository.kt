package com.example.pum_lista3.domain

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TodoListRepository {
  fun getAllLists(): Flow<List<TodoList>>

  fun getListById(id: String): Flow<TodoList>

  suspend fun addList(
    listNumber: Int,
    deadline: LocalDate,
    description: String,
    imageBitmap: Bitmap?
  )

  suspend fun updateList(todoList: TodoList)

  suspend fun deleteList(id: String)
}
