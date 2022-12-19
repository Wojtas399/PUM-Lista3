package com.example.pum_lista3.todoListCreator

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pum_lista3.domain.TodoList
import com.example.pum_lista3.domain.use_cases.AddListUseCase
import com.example.pum_lista3.domain.use_cases.GetTodoListUseCase
import com.example.pum_lista3.domain.use_cases.UpdateListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

enum class TodoListCreatorMode {
    Create,
    Edit,
}

enum class TodoListCreatorStatus {
    Initial,
    InProgress
}

data class TodoListCreatorState(
    val mode: TodoListCreatorMode = TodoListCreatorMode.Create,
    val status: TodoListCreatorStatus = TodoListCreatorStatus.Initial,
    val title: String? = null,
    val deadline: LocalDate? = null,
    val description: String? = null,
    val imageBitmap: Bitmap? = null,
)

@HiltViewModel
class TodoListCreatorViewModel @Inject constructor(
    private val addTodoListUseCase: AddListUseCase,
    private val getTodoListUseCase: GetTodoListUseCase,
    private val updateListUseCase: UpdateListUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TodoListCreatorState())
    private var _todoListId: String? = null

    val state: StateFlow<TodoListCreatorState> = _state.asStateFlow()

    fun initialize(todoListId: String?) {
        todoListId?.run {
            _state.update {
                it.copy(
                    mode = TodoListCreatorMode.Edit
                )
            }
            _todoListId = todoListId
            viewModelScope.launch { collectTodoList(todoListId) }
        }
    }

    fun changeTitle(title: String) {
        _state.update {
            it.copy(
                status = TodoListCreatorStatus.InProgress,
                title = title
            )
        }
    }

    fun changeDeadline(date: LocalDate) {
        _state.update {
            it.copy(
                status = TodoListCreatorStatus.InProgress,
                deadline = date,
            )
        }
    }

    fun changeDescription(description: String) {
        _state.update {
            it.copy(
                status = TodoListCreatorStatus.InProgress,
                description = description
            )
        }
    }

    fun changeImage(imageBitmap: Bitmap?) {
        _state.update {
            it.copy(
                imageBitmap = imageBitmap,
            )
        }
    }

    suspend fun submit() = withContext(Dispatchers.IO) {
        val title: String? = _state.value.title
        val deadline: LocalDate? = _state.value.deadline
        val description: String? = _state.value.description
        if (title != null && deadline != null && description != null) {
            doAppropriateSubmitOperation(
                title,
                deadline,
                description,
                _state.value.imageBitmap
            )
        }
    }

    private suspend fun collectTodoList(todoListId: String) {
        getTodoListUseCase(todoListId).collect { todoList ->
            _state.update { currentState ->
                currentState.copy(
                    title = todoList.title,
                    deadline = todoList.deadline,
                    description = todoList.description,
                    imageBitmap = todoList.imageBitmap,
                )
            }
        }
    }

    private suspend fun doAppropriateSubmitOperation(
        title: String,
        deadline: LocalDate,
        description: String,
        imageBitmap: Bitmap?,
    ) {
        _state.value.mode.run {
            when (this) {
                TodoListCreatorMode.Create -> addTodoListUseCase(
                    title = title,
                    deadline = deadline,
                    description = description,
                    imageBitmap = imageBitmap,
                )
                TodoListCreatorMode.Edit -> _todoListId?.run {
                    updateListUseCase(
                        todoList = TodoList(
                            id = this,
                            title = title,
                            deadline = deadline,
                            description = description,
                            imageBitmap = imageBitmap,
                        )
                    )
                }
            }
        }
    }
}