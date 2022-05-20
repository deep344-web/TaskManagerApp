package com.example.taskmanager

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val clickListener: OnItemClickListener,) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

     var taskslist = listOf<Task>()

    interface OnItemClickListener{
        fun onItemClicked(task: Task)
    }

    inner class TaskViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        val textbox : TextView = itemView.findViewById<TextView>(R.id.tasktext)
        val categoryTextView = itemview.findViewById<TextView>(R.id.taskCategoryText)
        val alarmOn = itemview.findViewById<ImageView>(R.id.alarmOn)
    }

    override fun getItemCount(): Int {
        return taskslist.size

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        var t : Task = taskslist[position]
        holder.apply {
            textbox.text = t.text
            categoryTextView.text = "Category : " + t.category
            if (t.isAlarmSet == true)
                alarmOn.visibility = View.VISIBLE

            else
                alarmOn.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val viewHolder =  TaskViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false))

        viewHolder.itemView.setOnClickListener{
            clickListener.onItemClicked(taskslist[viewHolder.adapterPosition])
        }

        return viewHolder
    }

    fun updateList(list : List<Task>)
    {
        taskslist = list
        notifyDataSetChanged()

    }
}