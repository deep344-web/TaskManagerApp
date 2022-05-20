package com.example.taskmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TaskViewModel( application: Application) : AndroidViewModel(application) {

    var allTasks: LiveData<List<Task>>
    var repository : TaskRepository

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskdao()
         repository = TaskRepository(taskDao)
        allTasks = repository.allTasks
    }

    fun delete(task : Task) = GlobalScope.launch(Dispatchers.IO) {
        repository.delete(task)
    }

    fun insert(task : Task) = GlobalScope.launch(Dispatchers.IO) {
        repository.insert(task)
    }

    fun update(task : Task) = GlobalScope.launch(Dispatchers.IO) {
        repository.update(task)
    }

    fun deleteAll() = GlobalScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }


}