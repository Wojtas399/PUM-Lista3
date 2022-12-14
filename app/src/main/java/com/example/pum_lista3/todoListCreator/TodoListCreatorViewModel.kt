package com.example.pum_lista3.todoListCreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val listNumber: Int? = null,
    val deadline: LocalDate? = null,
    val description: String? = null,
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
            _state.update { currentState ->
                currentState.copy(
                    mode = TodoListCreatorMode.Edit
                )
            }
            _todoListId = todoListId
            viewModelScope.launch { collectTodoList(todoListId) }
        }
    }

    fun changeListNumber(newNumber: Int) {
        _state.update { currentState ->
            currentState.copy(
                status = TodoListCreatorStatus.InProgress,
                listNumber = newNumber
            )
        }
    }

    fun changeDeadline(date: LocalDate) {
        _state.update { currentState ->
            currentState.copy(
                status = TodoListCreatorStatus.InProgress,
                deadline = date,
            )
        }
    }

    fun changeDescription(description: String) {
        _state.update { currentState ->
            currentState.copy(
                status = TodoListCreatorStatus.InProgress,
                description = description
            )
        }
    }

    suspend fun submit() = withContext(Dispatchers.IO) {
        val listNumber: Int? = _state.value.listNumber
        val deadline: LocalDate? = _state.value.deadline
        val description: String? = _state.value.description
        if (listNumber != null && deadline != null && description != null) {
            doAppropriateSubmitOperation(listNumber, deadline, description)
        }
    }

    private suspend fun collectTodoList(todoListId: String) {
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

    private suspend fun doAppropriateSubmitOperation(
        listNumber: Int,
        deadline: LocalDate,
        description: String,
    ) {
        _state.value.mode.run {
            when (this) {
                TodoListCreatorMode.Create -> addTodoListUseCase(
                    listNumber = listNumber,
                    deadline = deadline,
                    description = description,
                )
                TodoListCreatorMode.Edit -> _todoListId?.run {
                    updateListUseCase(
                        id = this,
                        listNumber = listNumber,
                        deadline = deadline,
                        description = description,
                    )
                }
            }
        }
    }
}