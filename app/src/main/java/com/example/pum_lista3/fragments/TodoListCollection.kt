package com.example.pum_lista3.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.pum_lista3.databinding.FragmentTodoListCollectionBinding

class TodoListCollection : Fragment() {
  private lateinit var binding: FragmentTodoListCollectionBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding =
      FragmentTodoListCollectionBinding.inflate(inflater, container, false)
    setFloatingButtonOnClickListener()
    return binding.root
  }

  private fun setFloatingButtonOnClickListener() {
    binding.floatingButton.setOnClickListener {
      navigateToTodoListCreator()
    }
  }

  private fun navigateToTodoListCreator() {
    val action =
      TodoListCollectionDirections.actionTodoListCollectionToTodoListCreator()
    Navigation.findNavController(binding.root).navigate(action)
  }
}