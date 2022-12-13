package com.example.pum_lista3.todoListsOverview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pum_lista3.R
import com.example.pum_lista3.domain.entities.TodoList
import com.example.pum_lista3.extensions.toUIFormat

class TodoListsOverviewAdapter(
  private val allTodoLists: List<TodoList>
) : RecyclerView.Adapter<TodoListsOverviewAdapter.TodoListsOverviewViewHolder>() {

  override fun getItemCount(): Int = allTodoLists.size

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListsOverviewViewHolder {
    return TodoListsOverviewViewHolder(
      LayoutInflater.from(parent.context).inflate(
        R.layout.todo_list_item,
        parent,
        false,
      )
    )
  }

  override fun onBindViewHolder(holder: TodoListsOverviewViewHolder, position: Int) {
    val todoList: TodoList = allTodoLists[position]
    holder.bind(todoList)
  }

  inner class TodoListsOverviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var titleTextView: TextView = view.findViewById(R.id.todoListTitle)
    private var deadlineTextView: TextView = view.findViewById(R.id.todoListDeadline)

    fun bind(todoList: TodoList) {
      val title = "Lista ${todoList.listNumber}"
      val subtitle = "Termin oddania: ${todoList.deadline.toUIFormat()}"
      titleTextView.text = title
      deadlineTextView.text = subtitle
    }
  }
}