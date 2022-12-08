package com.example.pum_lista3.domain.use_cases

import android.database.Observable
import com.example.pum_lista3.domain.entities.TodoList
import com.example.pum_lista3.domain.interfaces.TodoListInterface

class GetAllTodoListsUseCase(
  private val todoListInterface: TodoListInterface
) {
  operator fun invoke(): Observable<List<TodoList>?> {
    return todoListInterface.getAllLists()
  }
}