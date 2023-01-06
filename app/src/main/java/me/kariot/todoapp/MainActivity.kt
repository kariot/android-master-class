package me.kariot.todoapp

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import me.kariot.todoapp.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val PREF_NAME = "taskPref"
    val PREF_ARRAY = "prefArray"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initUI()
    }

    private fun initUI() {
        binding.fabAddItem.setOnClickListener {
            showInputAlert()
        }
    }

    private fun showInputAlert() {
        val alert = AlertDialog.Builder(this)
        val edittext = EditText(this)
        edittext.hint = "Enter Task"
        edittext.maxLines = 1
        val layout = FrameLayout(this)
        layout.setPaddingRelative(45, 15, 45, 15)
        alert.setTitle("Enter your task.")
        layout.addView(edittext)
        alert.setView(layout)
        alert.setPositiveButton("Save") { _, _ ->
            val task = edittext.text.toString()
            if (task.isNotEmpty()) {
                saveTask(task)
            }
        }
        alert.setNegativeButton("Cancel") { _, _ ->

        }
        alert.show()
    }

    private fun saveTask(task: String) {

        val jsonObject = JSONObject()
        jsonObject.put("task", task)
        jsonObject.put("isCompleted", false)

        val sharedPref = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val existingArray = sharedPref.getString(PREF_ARRAY, "[]") ?: "[]"
        val array = JSONArray(existingArray)
        array.put(jsonObject)

        editor.putString(PREF_ARRAY, array.toString())
        editor.apply()

    }
}