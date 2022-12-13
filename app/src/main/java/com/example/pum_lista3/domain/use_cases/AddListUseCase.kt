package com.example.pum_lista3.domain.use_cases

import com.example.pum_lista3.domain.interfaces.TodoListInterface
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject

class AddListUseCase @Inject constructor(
  private val todoListInterface: TodoListInterface
) {
  suspend operator fun invoke(
    listNumber: Int,
    deadline: LocalDate,
    description: String,
  ) {
    runBlocking {
      launch {
        todoListInterface.addList(
          listNumber = listNumber,
          deadline = deadline,
          description = description,
        )
      }
    }
  }
}