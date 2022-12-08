package com.example.pum_lista3.domain.use_cases

import android.database.Observable
import com.example.pum_lista3.domain.entities.TodoList
import com.example.pum_lista3.domain.interfaces.TodoListInterface

class GetTodoListUseCase(
  private val todoListInterface: TodoListInterface
) {
  operator fun invoke(
    id: String
  ): Observable<TodoList?> {
    return todoListInterface.getListById(id)
  }
}