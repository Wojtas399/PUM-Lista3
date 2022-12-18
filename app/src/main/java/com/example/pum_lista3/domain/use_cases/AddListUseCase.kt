package com.example.pum_lista3.domain.use_cases

import android.net.Uri
import com.example.pum_lista3.domain.TodoListRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject

class AddListUseCase @Inject constructor(
  private val todoListInterface: TodoListRepository
) {
  suspend operator fun invoke(
    listNumber: Int,
    deadline: LocalDate,
    description: String,
    imageUri: Uri?,
  ) {
    runBlocking {
      launch {
        todoListInterface.addList(
          listNumber = listNumber,
          deadline = deadline,
          description = description,
          imageUri = imageUri,
        )
      }
    }
  }
}