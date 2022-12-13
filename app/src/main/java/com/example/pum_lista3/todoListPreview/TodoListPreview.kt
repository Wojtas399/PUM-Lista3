package com.example.pum_lista3.todoListPreview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pum_lista3.databinding.FragmentTodoListPreviewBinding
import com.example.pum_lista3.extensions.toUIFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

    collectViewModel()
    val todoListId: String? = getTodoListIdFromArgs()
    if (todoListId != null) {
      viewModel.initialize(todoListId)
    }

    return binding.root
  }

  private fun getTodoListIdFromArgs(): String? {
    val bundle: Bundle = arguments ?: return null
    val args = TodoListPreviewArgs.fromBundle(bundle)
    return args.todoListId
  }

  private fun collectViewModel() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.state.collect { setContent(it) }
      }
    }
  }

  private fun setContent(state: TodoListPreviewState) {
    state.listNumber?.let { setTitle(it) }
    state.deadline?.let { setSubtitle(it) }
    state.description?.let { setDescription(it) }
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