package com.example.pum_lista3.domain.use_cases

import com.example.pum_lista3.domain.interfaces.TodoListInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

class UpdateListUseCase(
  private val todoListInterface: TodoListInterface
) {
  suspend operator fun invoke(
    id: String,
    listNumber: Int?,
    deadline: LocalDate?,
    description: String?,
  ) {
    runBlocking {
      launch {
        todoListInterface.updateList(
          id = id,
          listNumber = listNumber,
          deadline = deadline,
          description = description,
        )
      }
    }
  }
}