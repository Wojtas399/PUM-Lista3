package com.example.pum_lista3.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class RoomTodoList(
  @PrimaryKey @ColumnInfo(name = "entryId") var uid: String = UUID.randomUUID().toString(),
  @ColumnInfo(name = "title") val title: String,
  @ColumnInfo(name = "deadline") val deadline: String,
  @ColumnInfo(name = "description") val description: String,
  @ColumnInfo(name = "imageFilename") val imageFilename: String?,
)