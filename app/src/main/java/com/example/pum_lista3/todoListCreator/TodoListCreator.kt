package com.example.pum_lista3.todoListCreator

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.pum_lista3.databinding.FragmentTodoListCreatorImageActionSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

@AndroidEntryPoint
class TodoListCreator : Fragment() {
    private lateinit var binding: FragmentTodoListCreatorBinding
    private lateinit var imageActionSheetBinding: FragmentTodoListCreatorImageActionSheetBinding
    private lateinit var imageProvider: ImageProvider
    private lateinit var imageActionSheet: BottomSheetDialog
    private val viewModel: TodoListCreatorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListCreatorBinding.inflate(inflater, container, false)
        imageActionSheetBinding =
            FragmentTodoListCreatorImageActionSheetBinding.inflate(inflater, container, false)
        imageProvider = ImageProvider(requireContext(), requireActivity().activityResultRegistry)
        imageActionSheet = BottomSheetDialog(requireContext())

        setImageActionSheet()
        setMinDatePickerDate()

        collectViewModel()
        getTodoListIdFromArgs().run {
            viewModel.initialize(this)
        }

        setTitleValueListener()
        setDateValueListener()
        setDescriptionValueListener()
        setImageOnClickListener()
        setButtonOnClickListener()
        lifecycleScope.launch {
            imageProvider.selectedImageBitmap.collect {
                viewModel.changeImage(it)
            }
        }

        return binding.root
    }

    private fun setImageActionSheet() {
        imageActionSheetBinding.changeImageButton.setOnClickListener {
            imageActionSheet.dismiss()
            imageProvider.selectImage()
        }
        imageActionSheetBinding.deleteImageButton.setOnClickListener {
            imageActionSheet.dismiss()
            viewModel.changeImage(null)
        }
        imageActionSheetBinding.cancelButton.setOnClickListener {
            imageActionSheet.dismiss()
        }
        imageActionSheet.setContentView(imageActionSheetBinding.root)
    }

    private fun setMinDatePickerDate() {
        binding.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    private fun collectViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.takeIf { it.status == TodoListCreatorStatus.Initial }?.run {
                        setToolbarTitleAndButtonLabel(this.mode)
                        setInitialFormValues(this)
                    }
                    state.imageBitmap.run { setImage(this) }
                }
            }
        }
    }

    private fun getTodoListIdFromArgs(): String? {
        val bundle: Bundle = arguments ?: return null
        val args = TodoListCreatorArgs.fromBundle(bundle)
        return args.todoListId
    }

    private fun setTitleValueListener() {
        binding.listTitleInput.addTextChangedListener {
            viewModel.changeTitle(it.toString())
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
            if (viewModel.state.value.imageBitmap != null) {
                imageActionSheet.show()
            } else {
                imageProvider.selectImage()
            }
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
        todoListCreatorState.title?.run { setTitle(this) }
        todoListCreatorState.deadline.run { setDeadlineValue(this) }
        todoListCreatorState.description?.run { setDescriptionValue(this) }
    }

    private fun setImage(imageBitmap: Bitmap?) {
        if (imageBitmap == null) {
            binding.imageView.setImageResource(R.drawable.image_placeholder_48)
        } else {
            binding.imageView.setImageBitmap(imageBitmap)
        }
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

    private fun setTitle(title: String) {
        binding.listTitleInput.setText(title)
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