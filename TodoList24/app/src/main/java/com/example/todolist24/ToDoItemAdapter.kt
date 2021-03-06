package com.example.todolist24

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
class ToDoItemAdapter(context: Context, toDoItemList: MutableList<ToDoItem>) : BaseAdapter() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var itemList = toDoItemList
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val objectId: String = itemList.get(position).objectId as String
        val itemText: String = itemList.get(position).itemText as String
        val done: Boolean = itemList.get(position).done as Boolean
        val view: View
        val vh: ListRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.recycler_row, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }
        vh.label.text = itemText
        vh.checkbox.isChecked = done
        return view
    }
    override fun getItem(index: Int): Any {
        return itemList.get(index)
    }
    override fun getItemId(index: Int): Long {
        return index.toLong()
    }
    override fun getCount(): Int {
        return itemList.size
    }
    private class ListRowHolder(row: View?) {
        val label: TextView = row!!.findViewById<TextView>(R.id.todoTextView) as TextView
        val checkbox: CheckBox = row!!.findViewById<CheckBox>(R.id.checkbox) as CheckBox
        val ibDeleteObject: ImageButton = row!!.findViewById<ImageButton>(R.id.crossButton) as ImageButton
    }
}