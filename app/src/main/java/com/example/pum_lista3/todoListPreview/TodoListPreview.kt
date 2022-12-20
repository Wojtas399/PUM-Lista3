package com.example.pum_lista3.todoListPreview

import android.app.AlertDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.pum_lista3.R
import com.example.pum_lista3.databinding.FragmentTodoListPreviewBinding
import com.example.pum_lista3.extensions.toUIFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

@AndroidEntryPoint
class TodoListPreview : Fragment() {
    private lateinit var binding: FragmentTodoListPreviewBinding
    private val viewModel: TodoListPreviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListPreviewBinding.inflate(inflater, container, false)

        setToolbarTitle()
        setMenuProvider()
        setImageOnClickListener()
        setEditButtonOnClickListener()

        collectViewModel()
        getTodoListIdFromArgs()?.run {
            viewModel.initialize(this)
        }

        return binding.root
    }

    private fun setToolbarTitle() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = "Podgląd listy"
    }

    private fun setMenuProvider() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar)
            .addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    createDeleteButton(menu, menuInflater)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    onDeleteButtonPressed()
                    return true
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setImageOnClickListener() {
        binding.previewImage.setOnClickListener {
            navigateToImagePreview()
        }
    }

    private fun setEditButtonOnClickListener() {
        binding.editButton.setOnClickListener {
            viewModel.state.value.todoList?.run {
                val action = TodoListPreviewDirections.actionTodoListPreviewToTodoListCreator(
                    todoListId = this.id
                )
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    private fun collectViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { setContent(it) }
            }
        }
    }

    private fun getTodoListIdFromArgs(): String? {
        val bundle: Bundle = arguments ?: return null
        val args = TodoListPreviewArgs.fromBundle(bundle)
        return args.todoListId
    }

    private fun createDeleteButton(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.todo_list_preview_menu, menu)
        menu.findItem(R.id.todoListPreviewMenuDeleteIcon).icon?.let {
            DrawableCompat.setTint(
                it,
                ContextCompat.getColor(requireContext(), R.color.white)
            )
        }
    }

    private fun onDeleteButtonPressed() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        dialogBuilder.setMessage("Czy na pewno chcesz usunąć tę listę?").setCancelable(false)
            .setPositiveButton("Usuń") { _, _ ->
                runBlocking {
                    launch {
                        viewModel.deleteTodoList()
                    }
                }
                goToPreviousScreen()
            }.setNegativeButton("Anuluj") { dialog, _ -> dialog.dismiss() }
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun navigateToImagePreview() {
        val imageBitmap = viewModel.state.value.todoList?.imageBitmap
        imageBitmap?.run {
            val action = TodoListPreviewDirections.actionTodoListPreviewToImagePreview(
                imageBitmap = this
            )
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    private fun setContent(state: TodoListPreviewState) {
        state.todoList?.let {
            binding.previewTitle.text = it.title
            setSubtitle(it.deadline)
            binding.previewDescription.text = it.description
            it.imageBitmap.run { setImage(this) }
        }
    }

    private fun goToPreviousScreen() {
        findNavController().popBackStack()
    }

    private fun setSubtitle(deadline: LocalDate) {
        val text = "Termin zwrotu: ${deadline.toUIFormat()}"
        binding.previewSubtitle.text = text
    }

    private fun setImage(imageBitmap: Bitmap?) {
        if (imageBitmap == null) {
            binding.previewImage.setImageResource(R.drawable.image_placeholder_48)
        } else {
            binding.previewImage.setImageBitmap(imageBitmap)
        }
    }
}