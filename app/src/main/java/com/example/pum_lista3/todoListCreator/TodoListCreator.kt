package com.example.pum_lista3.todoListCreator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

        setupDropdownItem()

        collectViewModel()
        getTodoListIdFromArgs().run {
            viewModel.initialize(this)
        }

        setListNumberValueListener()
        setDateValueListener()
        setDescriptionValueListener()
        setImageOnClickListener()
        setButtonOnClickListener()

        return binding.root
    }

    private fun setupDropdownItem() {
        val listNumbers = resources.getStringArray(R.array.list_numbers)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, listNumbers)
        binding.listNumberInput.setAdapter(arrayAdapter)
    }

    private fun collectViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.takeIf { it.status == TodoListCreatorStatus.Initial }?.run {
                        setToolbarTitleAndButtonLabel(this.mode)
                        setInitialFormValues(this)
                    }
                }
            }
        }
    }

    private fun getTodoListIdFromArgs(): String? {
        val bundle: Bundle = arguments ?: return null
        val args = TodoListCreatorArgs.fromBundle(bundle)
        return args.todoListId
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

    private fun setImageOnClickListener() {
        binding.imageViewBackground.setOnClickListener {
            Log.d("ON CLICK LISTENER", "Image has been clicked!")
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

    private fun setToolbarTitleAndButtonLabel(creatorMode: TodoListCreatorMode) {
        creatorMode.run {
            setToolbarTitle(this)
            setSubmitButtonLabel(this)
        }
    }

    private fun setInitialFormValues(todoListCreatorState: TodoListCreatorState) {
        todoListCreatorState.listNumber?.run { setListNumberValue(this) }
        todoListCreatorState.deadline?.run { setDeadlineValue(this) }
        todoListCreatorState.description?.run { setDescriptionValue(this) }
    }

    private fun goBackToPreviousScreen() {
        findNavController().popBackStack()
    }

    private fun setToolbarTitle(creatorMode: TodoListCreatorMode) {
        val title = when (creatorMode) {
            TodoListCreatorMode.Create -> "Nowa lista"
            TodoListCreatorMode.Edit -> "Edycja listy"
        }
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = title
    }

    private fun setSubmitButtonLabel(creatorMode: TodoListCreatorMode) {
        binding.submitButton.text = when (creatorMode) {
            TodoListCreatorMode.Create -> "Dodaj"
            TodoListCreatorMode.Edit -> "Zapisz"
        }
    }

    private fun setListNumberValue(number: Int) {
        binding.listNumberInput.setText("$number", false)
    }

    private fun setDeadlineValue(deadline: LocalDate) {
        binding.datePicker.updateDate(
            deadline.year,
            deadline.monthValue - 1,
            deadline.dayOfMonth
        )
    }

    private fun setDescriptionValue(description: String) {
        binding.listDescriptionInput.setText(description)
    }
}