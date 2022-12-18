package com.example.pum_lista3.dependency_injection

import android.content.Context
import androidx.room.Room
import com.example.pum_lista3.data.TodoListRepositoryImpl
import com.example.pum_lista3.data.room.RoomTodoListDb
import com.example.pum_lista3.domain.TodoListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun provideTodoListDb(
    @ApplicationContext context: Context,
  ): RoomTodoListDb {
    return Room.databaseBuilder(
      context,
      RoomTodoListDb::class.java,
      "todo-list-db"
    ).build()
  }

  @Provides
  @Singleton
  fun provideTodoListInterface(
    roomTodoListDb: RoomTodoListDb
  ) : TodoListRepository {
    return TodoListRepositoryImpl(
      roomTodoListDao = roomTodoListDb.RoomTodoListDao()
    )
  }
}