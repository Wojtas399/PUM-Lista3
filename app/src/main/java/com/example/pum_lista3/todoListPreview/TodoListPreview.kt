package com.example.pum_lista3.todoListPreview

import android.app.AlertDialog
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
        collectViewModel()
        val todoListId: String? = getTodoListIdFromArgs()
        if (todoListId != null) {
            viewModel.initialize(todoListId)
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

    private fun setContent(state: TodoListPreviewState) {
        state.listNumber?.let { setTitle(it) }
        state.deadline?.let { setSubtitle(it) }
        state.description?.let { setDescription(it) }
    }

    private fun goToPreviousScreen() {
        findNavController().popBackStack()
    }

    private fun setTitle(listNumber: Int) {
        val text = "Lista $listNumber"
        binding.previewTitle.text = text
    }

    private fun setSubtitle(deadline: LocalDate) {
        val text = "Termin zwrotu: ${deadline.toUIFormat()}"
        binding.previewSubtitle.text = text
    }

    private fun setDescription(description: String) {
        binding.previewDescription.text = description
    }
}