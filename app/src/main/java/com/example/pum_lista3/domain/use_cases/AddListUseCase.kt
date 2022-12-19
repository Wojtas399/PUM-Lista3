package com.example.pum_lista3.domain.use_cases

import android.graphics.Bitmap
import com.example.pum_lista3.domain.TodoListRepository
import java.time.LocalDate
import javax.inject.Inject

class AddListUseCase @Inject constructor(
    private val todoListInterface: TodoListRepository
) {
    suspend operator fun invoke(
        title: String,
        deadline: LocalDate,
        description: String,
        imageBitmap: Bitmap?,
    ) {
        todoListInterface.addList(
            title = title,
            deadline = deadline,
            description = description,
            imageBitmap = imageBitmap,
        )
    }
}