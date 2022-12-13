package com.example.pum_lista3.todoListsOverview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pum_lista3.R
import com.example.pum_lista3.databinding.FragmentTodoListsOverviewBinding
import com.example.pum_lista3.domain.entities.TodoList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoListsOverview : Fragment() {
  private lateinit var binding: FragmentTodoListsOverviewBinding
  private val viewModel : TodoListsOverviewViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding =
      FragmentTodoListsOverviewBinding.inflate(inflater, container, false)

    collectViewModel()
    setFloatingButtonOnClickListener()

    return binding.root
  }

  private fun collectViewModel() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.state.collect {
          val allTodoLists: List<TodoList>? = it.allTodoLists
          if (allTodoLists != null) {
            setRecyclerView(allTodoLists)
          }
        }
      }
    }
  }

  private fun setFloatingButtonOnClickListener() {
    binding.floatingButton.setOnClickListener {
      navigateToTodoListCreator()
    }
  }

  private fun setRecyclerView(allTodoLists: List<TodoList>) {
    binding.recyclerView.apply {
      adapter = TodoListsOverviewAdapter(allTodoLists)
      layoutManager = LinearLayoutManager(requireContext())
    }
  }

  private fun navigateToTodoListCreator() {
    Navigation.findNavController(binding.root).navigate(
      R.id.action_todoListsOverview_to_todoListCreator
    )
  }
}