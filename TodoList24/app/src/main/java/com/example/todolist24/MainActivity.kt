package com.example.todolist24

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class ToDoItem {
    override fun toString(): String {
        return "${this.objectId}   ${this.itemText}   ${this.done}"
    }
    companion object Factory {
        fun create(): ToDoItem = ToDoItem()
    }
    var objectId: String? = null
    var itemText: String? = null
    var done: Boolean? = false
}

class MainActivity : AppCompatActivity() {
    var todoList = ArrayList<ToDoItem>()
    lateinit var mDatabase: DatabaseReference
    lateinit var adapter: RecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val layoutManager = LinearLayoutManager(this)
        var adapter =  RecyclerAdapter(todoList)
        recyclerView.adapter=adapter
        recyclerView.setLayoutManager(GridLayoutManager(this, 2))

        val addItemButton = findViewById<View>(R.id.addItemButton) as FloatingActionButton
        //recyclerViewItems = findViewById<View>(R.id.recyclerView) as RecyclerView

        addItemButton.setOnClickListener { view ->
            //Show Dialog here to add new Item
            addNewItemDialog()
        }
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.keepSynced(true)
        var getData = object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError){
            }
            override fun onDataChange(p0: DataSnapshot) {
                addDataToList(p0)
            }
        }
        mDatabase.addValueEventListener(getData)
        mDatabase.addListenerForSingleValueEvent(getData)


    }

    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (items.hasNext()) {
            val toDoListindex = items.next()
            val itemsIterator = toDoListindex.children.iterator()

            //check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {
                //get current item
                val currentItem = itemsIterator.next()
                val todoItem = ToDoItem.create()
                //get current data in a map
                //key will return Firebase ID
                todoItem.objectId = currentItem.key
                todoItem.done = currentItem.child("done").getValue() as Boolean?
                todoItem.itemText = currentItem.child("itemText").getValue() as String?
                todoList.add(todoItem)
                println(todoList.toString())
            }
        }
        var adapter =  RecyclerAdapter(todoList)
        recyclerView.adapter=adapter
        //alert adapter that has changed
        //adapter.notifyDataSetChanged()
    }

    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)
        val itemEditText = EditText(this)
        alert.setTitle("Enter your new plan")
        alert.setMessage("Add")
        alert.setView(itemEditText)
        alert.setPositiveButton("Submit") { dialog, positiveButton ->
            val todoItem = ToDoItem.create()
            todoItem.itemText = itemEditText.text.toString()
            todoItem.done = false
            todoList.add(todoItem)
            //We first make a push so that a new item is made with a unique ID
            val newItem = mDatabase.child(Constants.FIREBASE_ITEM).push()
            todoItem.objectId = newItem.key
            //then, we used the reference to set the value on that ID
            newItem.setValue(todoItem)
            dialog.dismiss()
            Toast.makeText(this, todoItem.itemText + " is saved.", Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }
}

