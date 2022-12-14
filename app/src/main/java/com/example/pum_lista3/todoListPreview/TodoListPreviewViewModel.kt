package com.example.pum_lista3.todoListPreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pum_lista3.domain.entities.TodoList
import com.example.pum_lista3.domain.use_cases.DeleteListUseCase
import com.example.pum_lista3.domain.use_cases.GetTodoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class TodoListPreviewState(
    val todoList: TodoList? = null
)

@HiltViewModel
class TodoListPreviewViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val deleteListUseCase: DeleteListUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TodoListPreviewState())
    private var _todoListId: String? = null

    val state: StateFlow<TodoListPreviewState> = _state.asStateFlow()

    fun initialize(todoListId: String) {
        _todoListId = todoListId
        viewModelScope.launch {
            getTodoListUseCase(todoListId).collect { todoList ->
                _state.update { currentState ->
                    currentState.copy(
                        todoList = todoList
                    )
                }
            }
        }
    }

    suspend fun deleteTodoList() = withContext(Dispatchers.IO) {
        _todoListId?.run { deleteListUseCase(this) }
    }
}
