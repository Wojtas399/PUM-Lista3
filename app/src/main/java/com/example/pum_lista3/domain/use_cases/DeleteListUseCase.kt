package com.example.pum_lista3.domain.use_cases

import com.example.pum_lista3.domain.interfaces.TodoListInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DeleteListUseCase @Inject constructor(
  private val todoListInterface: TodoListInterface
) {
  suspend operator fun invoke(
    id: String
  ) {
    runBlocking {
      launch {
        todoListInterface.deleteList(id)
      }
    }
  }
}