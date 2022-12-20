package com.example.pum_lista3.domain

import android.graphics.Bitmap
import java.time.LocalDate

data class TodoList(
  val id: String,
  val title: String,
  val deadline: LocalDate,
  val description: String,
  val imageBitmap: Bitmap?
)
