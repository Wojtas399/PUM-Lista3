package com.example.pum_lista3.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomTodoList::class], version = 1)
abstract class RoomTodoListDb : RoomDatabase() {
  abstract fun RoomTodoListDao(): RoomTodoListDao
}