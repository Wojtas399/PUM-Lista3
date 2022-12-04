package com.example.pum_lista3.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pum_lista3.accessObjects.ToDoListDao
import com.example.pum_lista3.entities.ToDoList

@Database(entities = [ToDoList::class], version = 1)
abstract class ToDoListDb : RoomDatabase() {
  abstract fun ToDoListDao(): ToDoListDao
}