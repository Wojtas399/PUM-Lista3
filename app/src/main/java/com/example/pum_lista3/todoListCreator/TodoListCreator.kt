package com.example.pum_lista3.todoListCreator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.pum_lista3.R
import com.example.pum_lista3.databinding.FragmentTodoListCreatorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

@AndroidEntryPoint
class TodoListCreator : Fragment() {
    private lateinit var binding: FragmentTodoListCreatorBinding
    private val viewModel: TodoListCreatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListCreatorBinding.inflate(inflater, container, false)

        setToolbarTitle()
        setupDropdownItem()
        setListNumberValueListener()
        setDateValueListener()
        setDescriptionValueListener()
        setButtonOnClickListener()

        return binding.root
    }

    private fun setToolbarTitle() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = "Nowa lista"
    }

    private fun setupDropdownItem() {
        val listNumbers = resources.getStringArray(R.array.list_numbers)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, listNumbers)
        binding.listNumberInput.setAdapter(arrayAdapter)
    }

    private fun setListNumberValueListener() {
        binding.listNumberInput.setOnItemClickListener { _, _, itemIndex, _ ->
            viewModel.changeListNumber(itemIndex + 1)
        }
    }

    private fun setDateValueListener() {
        binding.datePicker.setOnDateChangedListener { _, year, month, day ->
            val date: LocalDate = LocalDate.of(year, month + 1, day)
            viewModel.changeDeadline(date)
        }
    }

    private fun setDescriptionValueListener() {
        binding.listDescriptionInput.addTextChangedListener {
            viewModel.changeDescription(it.toString())
        }
    }

    private fun setButtonOnClickListener() {
        binding.submitButton.setOnClickListener {
            runBlocking {
                launch {
                    viewModel.submit()
                }
            }
            goBackToPreviousScreen()
        }
    }

    private fun goBackToPreviousScreen() {
        findNavController().popBackStack()
    }
}