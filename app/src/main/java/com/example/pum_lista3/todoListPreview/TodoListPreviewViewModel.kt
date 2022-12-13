package com.example.pum_lista3.todoListPreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pum_lista3.domain.use_cases.GetTodoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TodoListPreviewState(
  val listNumber: Int? = null,
  val deadline: LocalDate? = null,
  val description: String? = null,
)

@HiltViewModel
class TodoListPreviewViewModel @Inject constructor(
  private val getTodoListUseCase: GetTodoListUseCase
) : ViewModel() {
  private val _state = MutableStateFlow(TodoListPreviewState())

  val state : StateFlow<TodoListPreviewState> = _state.asStateFlow()

  fun initialize(todoListId: String) {
    viewModelScope.launch {
      getTodoListUseCase(todoListId).collect { todoList ->
        _state.update { currentState ->
          currentState.copy(
            listNumber = todoList.listNumber,
            deadline = todoList.deadline,
            description = todoList.description,
          )
        }
      }
    }
  }
}
