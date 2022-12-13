package com.example.pum_lista3.domain.entities

import java.time.LocalDate

data class TodoList(
  val id: String,
  val listNumber: Int,
  val deadline: LocalDate,
  val description: String,
)
