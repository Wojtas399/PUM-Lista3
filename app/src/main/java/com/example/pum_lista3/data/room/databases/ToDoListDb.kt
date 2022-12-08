package com.example.pum_lista3.data.room.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pum_lista3.data.room.accessObjects.ToDoListDao
import com.example.pum_lista3.data.room.entities.ToDoList

@Database(entities = [ToDoList::class], version = 1)
abstract class ToDoListDb : RoomDatabase() {
  abstract fun ToDoListDao(): ToDoListDao
}