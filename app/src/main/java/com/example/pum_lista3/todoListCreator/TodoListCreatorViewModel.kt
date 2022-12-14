package com.example.pum_lista3.todoListCreator

import androidx.lifecycle.ViewModel
import com.example.pum_lista3.domain.use_cases.AddListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

data class TodoListCreatorState(
    val listNumber: Int? = null,
    val deadline: LocalDate? = null,
    val description: String? = null,
)

@HiltViewModel
class TodoListCreatorViewModel @Inject constructor(
    private val addTodoListUseCase: AddListUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TodoListCreatorState())

    fun changeListNumber(newNumber: Int) {
        _state.update { currentState ->
            currentState.copy(
                listNumber = newNumber
            )
        }
    }

    fun changeDeadline(date: LocalDate) {
        _state.update { currentState ->
            currentState.copy(
                deadline = date,
            )
        }
    }

    fun changeDescription(description: String) {
        _state.update { currentState ->
            currentState.copy(
                description = description
            )
        }
    }

    suspend fun submit() = withContext(Dispatchers.IO) {
        val listNumber: Int? = _state.value.listNumber
        val deadline: LocalDate? = _state.value.deadline
        val description: String? = _state.value.description
        if (listNumber != null && deadline != null && description != null) {
            addTodoListUseCase(
                listNumber = listNumber,
                deadline = deadline,
                description = description,
            )
        }
    }
}