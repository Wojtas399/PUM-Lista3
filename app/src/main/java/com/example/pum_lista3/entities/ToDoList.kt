package com.example.pum_lista3.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoList(
  @PrimaryKey val uid: Int,
  @ColumnInfo(name = "listNumber") val listNumber: Int,
  @ColumnInfo(name = "deadline") val deadline: String,
  @ColumnInfo(name = "description") val description: String,
)