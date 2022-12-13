package com.example.pum_lista3.data.repositories

import com.example.pum_lista3.domain.interfaces.TodoListInterface
import com.example.pum_lista3.data.room.TodoListDb
import com.example.pum_lista3.data.room.TodoListDbModel
import com.example.pum_lista3.domain.entities.TodoList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TodoListRepository @Inject constructor(
  val db: TodoListDb,
  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoListInterface {
  override fun getAllLists(): Flow<List<TodoList>> {
    return db.TodoListDao().getAll().map { todoLists ->
      todoLists.map { mapFromDbModelToDomainModel(it) }
    }
  }

  override fun getListById(id: String): Flow<TodoList> {
    return db.TodoListDao().getById(id).map { mapFromDbModelToDomainModel(it) }
  }

  override suspend fun addList(
    listNumber: Int,
    deadline: LocalDate,
    description: String
  ) = withContext(ioDispatcher) {
    db.TodoListDao().insertToDoList(
      TodoListDbModel(
        listNumber = listNumber,
        deadline = deadline.toString(),
        description = description
      )
    )
  }

  override suspend fun updateList(
    id: String,
    listNumber: Int?,
    deadline: LocalDate?,
    description: String?
  ) = withContext(ioDispatcher) {
    val todoList: TodoList = getListById(id).first()
    val updatedTodoList = TodoList(
      id = id,
      listNumber = listNumber ?: todoList.listNumber,
      deadline = deadline ?: todoList.deadline,
      description = description ?: todoList.description,
    )
    db.TodoListDao().updateToDoList(
      TodoListDbModel(
        uid = updatedTodoList.id,
        listNumber = updatedTodoList.listNumber,
        deadline = updatedTodoList.deadline.toString(),
        description = updatedTodoList.description,
      ),
    )
  }

  override suspend fun deleteList(id: String) = withContext(ioDispatcher) {
    db.TodoListDao().deleteToDoListById(id)
  }

  private fun mapFromDbModelToDomainModel(dbTodoList: TodoListDbModel): TodoList {
    val deadlineInArray = dbTodoList.deadline.split('-')
    return TodoList(
      id = dbTodoList.uid,
      listNumber = dbTodoList.listNumber,
      deadline = LocalDate.of(
        deadlineInArray[0].toInt(),
        deadlineInArray[1].toInt(),
        deadlineInArray[2].toInt(),
      ),
      description = dbTodoList.description,
    )
  }
}