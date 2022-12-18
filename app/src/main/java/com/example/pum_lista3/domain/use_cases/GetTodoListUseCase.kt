package com.example.pum_lista3.domain.use_cases

import com.example.pum_lista3.domain.TodoList
import com.example.pum_lista3.domain.TodoListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodoListUseCase @Inject constructor(
  private val todoListInterface: TodoListRepository
) {
  operator fun invoke(id: String): Flow<TodoList> {
    return todoListInterface.getListById(id)
  }
}