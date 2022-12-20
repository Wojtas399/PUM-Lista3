package com.example.pum_lista3.extensions

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.toUIFormat() : String {
  val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
  return this.format(formatter)
}