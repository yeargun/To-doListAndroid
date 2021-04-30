package com.example.todolist24

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_row.view.*

class RecyclerAdapter(val todoList: ArrayList<ToDoItem>) : RecyclerView.Adapter<RecyclerAdapter.TodoList>() {
    class TodoList(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoList {
        //inflater
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row, parent, false)
        return TodoList(itemView)
    }


    override fun getItemCount(): Int {
        return todoList.size
    }

    override fun onBindViewHolder(holder: TodoList, position: Int) {
        holder.itemView.todoTextView.append(todoList.get(position).itemText)

    }

}