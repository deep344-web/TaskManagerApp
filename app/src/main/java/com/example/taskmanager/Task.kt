package com.example.taskmanager

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Task_table")
class Task (
    @ColumnInfo(name= "task_name")
    var text : String,
    @ColumnInfo(name = "task_category")
    var category : String = "Default",

    var isAlarmSet : Boolean = false,
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}