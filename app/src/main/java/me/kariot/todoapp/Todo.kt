package me.kariot.todoapp

data class Todo (
    val todo: String,

    var isChecked: Boolean = false,
    var id: String
)