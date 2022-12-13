package com.example.pum_lista3.dependency_injection

import android.content.Context
import androidx.room.Room
import com.example.pum_lista3.data.repositories.TodoListRepository
import com.example.pum_lista3.data.room.databases.ToDoListDb
import com.example.pum_lista3.domain.interfaces.TodoListInterface
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
  ): ToDoListDb {
    return Room.databaseBuilder(
      context,
      ToDoListDb::class.java,
      "todo-list-db"
    ).build()
  }

  @Provides
  @Singleton
  fun provideTodoListInterface(
    todoListDb: ToDoListDb
  ) : TodoListInterface {
    return TodoListRepository(
      db = todoListDb
    )
  }
}