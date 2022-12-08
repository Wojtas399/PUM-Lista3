package com.example.pum_lista3.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.pum_lista3.R
import com.example.pum_lista3.databinding.FragmentTodoListCreatorBinding
import java.time.LocalDate

class TodoListCreator : Fragment() {
  private lateinit var binding: FragmentTodoListCreatorBinding
  var listNumber: Int? = null
  var date: LocalDate? = null
  var description: String? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentTodoListCreatorBinding.inflate(inflater, container, false)

    setupDropdownItem()
    setListNumberValueListener()
    setDateValueListener()
    setDescriptionValueListener()

    return binding.root
  }

  private fun setupDropdownItem() {
    val listNumbers = resources.getStringArray(R.array.list_numbers)
    val arrayAdapter =
      ArrayAdapter(requireContext(), R.layout.dropdown_item, listNumbers)
    binding.listNumberInput.setAdapter(arrayAdapter)
  }

  private fun setListNumberValueListener() {
    binding.listNumberInput.setOnItemClickListener { _, _, itemIndex, _ ->
      listNumber = itemIndex + 1
    }
  }

  private fun setDateValueListener() {
    binding.datePicker.setOnDateChangedListener { _, year, month, day ->
      date = LocalDate.of(year, month, day)
    }
  }

  private fun setDescriptionValueListener() {
    binding.listDescriptionInput.addTextChangedListener {
      description = it.toString()
    }
  }
}