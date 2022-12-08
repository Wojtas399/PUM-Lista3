package com.example.pum_lista3.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.pum_lista3.R
import com.example.pum_lista3.databinding.FragmentTodoListCreatorBinding

class TodoListCreator : Fragment() {
  private lateinit var binding: FragmentTodoListCreatorBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentTodoListCreatorBinding.inflate(inflater, container, false)

    setupDropdownItem()

    return binding.root
  }

  private fun setupDropdownItem() {
    val listNumbers = resources.getStringArray(R.array.list_numbers)
    val arrayAdapter =
      ArrayAdapter(requireContext(), R.layout.dropdown_item, listNumbers)
    binding.todoListNumberTextView.setAdapter(arrayAdapter)
  }
}