package com.example.pum_lista3.data

import android.graphics.Bitmap
import com.example.pum_lista3.data.internal_storage.InternalStorageImage
import com.example.pum_lista3.data.internal_storage.InternalStorageImageService
import com.example.pum_lista3.domain.TodoListRepository
import com.example.pum_lista3.data.room.RoomTodoList
import com.example.pum_lista3.data.room.RoomTodoListDao
import com.example.pum_lista3.domain.TodoList
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class TodoListRepositoryImpl @Inject constructor(
    private val roomTodoListDao: RoomTodoListDao,
    private val internalStorageImageService: InternalStorageImageService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoListRepository {
    override fun getAllLists(): Flow<List<TodoList>> {
        return roomTodoListDao.getAll().map { todoLists ->
            todoLists.map { combineRoomTodoListWithImage(it) }
        }
    }

    override fun getListById(id: String): Flow<TodoList> {
        return roomTodoListDao.getById(id).map { combineRoomTodoListWithImage(it) }
    }

    override suspend fun addList(
        listNumber: Int,
        deadline: LocalDate,
        description: String,
        imageBitmap: Bitmap?,
    ) = withContext(ioDispatcher) {
        var imageFilename: String? = null
        imageBitmap?.let {
            imageFilename = saveImageToInternalStorage(it)
        }
        roomTodoListDao.insertToDoList(
            RoomTodoList(
                listNumber = listNumber,
                deadline = deadline.toString(),
                description = description,
                imageFilename = imageFilename,
            )
        )
    }

    override suspend fun updateList(todoList: TodoList) = withContext(ioDispatcher) {
        val currentRoomTodoList: RoomTodoList = roomTodoListDao.getById(todoList.id).first()
        currentRoomTodoList.imageFilename?.let {
            internalStorageImageService.deleteImage(it)
        }
        var imageFilename: String? = null
        todoList.imageBitmap?.let {
            imageFilename = saveImageToInternalStorage(it)
        }
        roomTodoListDao.updateToDoList(
            mapFromDomainModelToDbModel(todoList, imageFilename),
        )
    }

    override suspend fun deleteList(id: String) = withContext(ioDispatcher) {
        roomTodoListDao.deleteToDoListById(id)
    }

    private suspend fun combineRoomTodoListWithImage(roomTodoList: RoomTodoList): TodoList {
        var image: InternalStorageImage? = null
        roomTodoList.imageFilename?.let {
            image = internalStorageImageService.loadImage(it)
        }
        return mapFromDbModelToDomainModel(roomTodoList, image)
    }

    private fun saveImageToInternalStorage(imageBitmap: Bitmap): String {
        val imageFilename = UUID.randomUUID().toString()
        imageFilename.let { filename ->
            internalStorageImageService.saveImage(filename, imageBitmap)
        }
        return imageFilename
    }

    private fun mapFromDbModelToDomainModel(
        roomTodoList: RoomTodoList,
        image: InternalStorageImage?,
    ): TodoList {
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
            imageBitmap = image?.bitmap,
        )
    }

    private fun mapFromDomainModelToDbModel(
        todoList: TodoList,
        imageFilename: String?,
    ): RoomTodoList {
        return RoomTodoList(
            uid = todoList.id,
            listNumber = todoList.listNumber,
            deadline = todoList.deadline.toString(),
            description = todoList.description,
            imageFilename = imageFilename,
        )
    }
}