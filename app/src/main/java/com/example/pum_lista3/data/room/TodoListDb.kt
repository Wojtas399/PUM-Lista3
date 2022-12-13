package com.example.pum_lista3.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TodoListDbModel::class], version = 1)
abstract class TodoListDb : RoomDatabase() {
  abstract fun TodoListDao(): TodoListDao
}