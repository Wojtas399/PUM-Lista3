package com.example.pum_lista3.domain.use_cases

import com.example.pum_lista3.domain.entities.TodoList
import com.example.pum_lista3.domain.interfaces.TodoListInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTodoListsUseCase @Inject constructor(
  private val todoListInterface: TodoListInterface
) {
  operator fun invoke(): Flow<List<TodoList>> {
    return todoListInterface.getAllLists()
  }
}