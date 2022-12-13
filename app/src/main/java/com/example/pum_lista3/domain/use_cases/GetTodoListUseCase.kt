package com.example.pum_lista3.domain.use_cases

import com.example.pum_lista3.domain.entities.TodoList
import com.example.pum_lista3.domain.interfaces.TodoListInterface
import kotlinx.coroutines.flow.Flow

class GetTodoListUseCase(
  private val todoListInterface: TodoListInterface
) {
  operator fun invoke(
    id: String
  ): Flow<TodoList?> {
    return todoListInterface.getListById(id)
  }
}