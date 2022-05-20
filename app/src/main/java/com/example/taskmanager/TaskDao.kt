package com.example.taskmanager

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(task : Task)

    @Delete
    fun delete(task : Task)

    @Query("select * from task_table order by id Desc")
    fun getAllTasks() : LiveData<List<Task>>

    @Update
    fun update(task : Task)

    @Query("DELETE FROM task_table")
    fun  deleteAll()
}
