package com.example.pum_lista3.domain.use_cases

import com.example.pum_lista3.domain.TodoListRepository
import javax.inject.Inject

class DeleteListUseCase @Inject constructor(
    private val todoListInterface: TodoListRepository
) {
    suspend operator fun invoke(
        id: String
    ) {
        todoListInterface.deleteList(id)
    }
}