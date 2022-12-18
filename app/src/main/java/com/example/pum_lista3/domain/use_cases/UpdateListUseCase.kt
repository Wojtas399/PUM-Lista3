package com.example.pum_lista3.domain.use_cases

import com.example.pum_lista3.domain.TodoList
import com.example.pum_lista3.domain.TodoListRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UpdateListUseCase @Inject constructor(
  private val todoListInterface: TodoListRepository
) {
  suspend operator fun invoke(todoList: TodoList) {
    runBlocking {
      launch {
        todoListInterface.updateList(todoList)
      }
    }
  }
}