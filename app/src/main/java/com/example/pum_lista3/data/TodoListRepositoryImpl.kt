package com.example.pum_lista3.data

import android.net.Uri
import com.example.pum_lista3.domain.TodoListRepository
import com.example.pum_lista3.data.room.RoomTodoList
import com.example.pum_lista3.data.room.RoomTodoListDao
import com.example.pum_lista3.domain.TodoList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TodoListRepositoryImpl @Inject constructor(
  private val roomTodoListDao: RoomTodoListDao,
  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoListRepository {
  override fun getAllLists(): Flow<List<TodoList>> {
    return roomTodoListDao.getAll().map { todoLists ->
      todoLists.map { mapFromDbModelToDomainModel(it) }
    }
  }

  override fun getListById(id: String): Flow<TodoList> {
    return roomTodoListDao.getById(id).map { mapFromDbModelToDomainModel(it) }
  }

  override suspend fun addList(
    listNumber: Int,
    deadline: LocalDate,
    description: String,
    imageUri: Uri?,
  ) = withContext(ioDispatcher) {
    roomTodoListDao.insertToDoList(
      RoomTodoList(
        listNumber = listNumber,
        deadline = deadline.toString(),
        description = description,
        imageUriAsString = imageUri?.toString(),
      )
    )
  }

  override suspend fun updateList(todoList: TodoList) = withContext(ioDispatcher) {
    roomTodoListDao.updateToDoList(
      mapFromDomainModelToDbModel(todoList),
    )
  }

  override suspend fun deleteList(id: String) = withContext(ioDispatcher) {
    roomTodoListDao.deleteToDoListById(id)
  }

  private fun mapFromDbModelToDomainModel(roomTodoList: RoomTodoList): TodoList {
    val deadlineInArray = roomTodoList.deadline.split('-')
    return TodoList(
      id = roomTodoList.uid,
      listNumber = roomTodoList.listNumber,
      deadline = LocalDate.of(
        deadlineInArray[0].toInt(),
        deadlineInArray[1].toInt(),
        deadlineInArray[2].toInt(),
      ),
      description = roomTodoList.description,
      imageUri = Uri.parse(roomTodoList.imageUriAsString),
    )
  }

  private fun mapFromDomainModelToDbModel(todoList: TodoList): RoomTodoList {
    return RoomTodoList(
      uid = todoList.id,
      listNumber = todoList.listNumber,
      deadline = todoList.deadline.toString(),
      description = todoList.description,
      imageUriAsString = todoList.imageUri?.toString(),
    )
  }
}