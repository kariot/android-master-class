package me.kariot.todoapp

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_task_item.view.*
import me.kariot.todoapp.Constants.Companion.PREF_ARRAY
import me.kariot.todoapp.Constants.Companion.PREF_NAME
import org.json.JSONArray
import org.json.JSONObject

//A RecyclerView is essentially a ViewGroup of containers
// called ViewHolders which populate a particular item.

//adapter bind the set of data objects to the views shown in the ViewHolders
//step 1: create an adapter class with params that accept todo list
//This adapter class should inherit from RecyclerView.Adapter
class TodoAdapter(private val todos: MutableList<Todo>) :
    RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    //a ViewHolder populate the UI from the xml item file
    //step 2: create an inner class for ViewHolder that accept reference to the view that it holds
    //This viewholder class should inherit from RecyclerView.ViewHolder
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    //step 3: implement the abstract functions
    //step 3.1 onCreateViewHolder() creates a new ViewHolder object whenever the RecyclerView needs a new one.
    //here we need to return layout of the view holder
    //in order to get the kotlin version of the xml layout file, use layout inflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_task_item, parent, false
            )
        )
    }

    //step 3.3 onBindViewHolder() takes the ViewHolder object and sets the
    // proper list data for the particular row on the views inside.
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos.get(position)
        //.apply is a shorthand to kotlin to prepend the LHS
        holder.itemView.apply {
            textView.text = currentTodo.todo
            cbDone.isChecked = currentTodo.isChecked

            //step 7.
            toggleStrikeThrough(tvTodo = textView, currentTodo.isChecked)
            cbDone.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(textView, isChecked)
                currentTodo.isChecked = !currentTodo.isChecked
                updateLocalData(this.context, currentTodo, isChecked)
            }
        }

    }

    private fun updateLocalData(context: Context, currentTodo: Todo, isChecked: Boolean) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        //gets saved array of tudos
        val existingArray = sharedPref.getString(PREF_ARRAY, "[]") ?: "[]"
        //converts to JSONArray
        val array = JSONArray(existingArray)
        //array to store new items
        val newArray = JSONArray()
        for (i in 0 until array.length()) { //loops over all available todos
            val json = array.get(i) as? JSONObject // gets each todos as JSONObject.
            json?.let {//null safety check
                val currentItemId = it.getString("id") //gets id of iterating todo
                if (currentItemId == currentTodo.id) { // identifies clicked item
                    it.put("isChecked", isChecked) // updates status of selected todo
                }
                newArray.put(json)//adds iterating/updated item to new list
            }
        }
        //saves updated list to array
        val editor = sharedPref.edit()
        editor.putString(PREF_ARRAY, newArray.toString())
        editor.apply()
    }

    //step 3.2 returns the number of items in the list
    override fun getItemCount(): Int {
        return todos.size
    }

    //step 4. function to add new todoitem to list
    fun addTodo(todo: Todo) {
        todos.add(todo)
        notifyItemInserted(todos.size - 1)
    }

    //step 5. function to delete todos
    fun deleteDoneTodos() {
        todos.removeAll { todo ->
            todo.isChecked //condition to remove from the list
        }
        notifyDataSetChanged()
    }

    //step 6. function to strikethrough text
    private fun toggleStrikeThrough(tvTodo: TextView, isChecked: Boolean) {
        if (isChecked) {
            tvTodo.paintFlags = tvTodo.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTodo.paintFlags = tvTodo.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    fun clearAll() {
        this.todos.clear()//remove all elements from list
        notifyDataSetChanged()//reloads the list, hence list is empty no ui will be shown
    }
}