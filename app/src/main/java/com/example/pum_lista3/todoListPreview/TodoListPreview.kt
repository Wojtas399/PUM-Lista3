package com.example.pum_lista3.todoListPreview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pum_lista3.databinding.FragmentTodoListPreviewBinding

class TodoListPreview : Fragment() {
  private lateinit var binding: FragmentTodoListPreviewBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentTodoListPreviewBinding.inflate(inflater, container, false)

    val todoListId: String? = getTodoListIdFromArgs()
    Log.d("TODO LIST ID:", todoListId.toString())

    return binding.root
  }

  private fun getTodoListIdFromArgs() : String? {
    val bundle : Bundle = arguments ?: return null
    val args = TodoListPreviewArgs.fromBundle(bundle)
    return args.todoListId
  }
}