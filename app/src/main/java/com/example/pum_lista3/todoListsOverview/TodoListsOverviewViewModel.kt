package com.example.pum_lista3.todoListsOverview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pum_lista3.domain.TodoList
import com.example.pum_lista3.domain.use_cases.GetAllTodoListsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TodoListsOverviewState(
  val allTodoLists: List<TodoList>? = null
)

@HiltViewModel
class TodoListsOverviewViewModel @Inject constructor(
  private val getAllTodoListsUseCase: GetAllTodoListsUseCase,
) : ViewModel() {
  private val _state = MutableStateFlow(TodoListsOverviewState())

  val state: StateFlow<TodoListsOverviewState> = _state.asStateFlow()

  init {
    viewModelScope.launch {
      getAllTodoListsUseCase().collect { allTodoLists ->
        _state.update { currentState ->
          currentState.copy(
            allTodoLists = allTodoLists
          )
        }
      }
    }
  }
}
