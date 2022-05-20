package com.example.taskmanager

import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class MainActivity : AppCompatActivity(), TaskAdapter.OnItemClickListener {
    lateinit var binding : ActivityMainBinding
    lateinit var viewModel : TaskViewModel
    lateinit var adapter : TaskAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(this)
        binding.tasksRecyclerView.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(TaskViewModel::class.java)

        viewModel.allTasks.observe(this, {
            it?.let {
                if (it.size == 0)
                    binding.noTaskView.visibility = View.VISIBLE

                else
                    binding.noTaskView.visibility = View.GONE

                adapter.updateList(it)
            }
        })

        setUpSwipeGesture()

        binding.addNewTaskIntent.setOnClickListener {
            addNewTask()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.deleteAllTasks ->{
                viewModel.deleteAll()
                true
            }
            else -> {
                true
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun addNewTask()
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add new task")
        val layout = layoutInflater.inflate(R.layout.alert_dialog, null)
        builder.setView(layout)
        val taskNamelayout = layout.findViewById<TextInputEditText>(R.id.titleEt)
        val categoryNamelayout = layout.findViewById<TextInputEditText>(R.id.categoryEt)
        val checkBox = layout.findViewById<CheckBox>(R.id.setNotification)
        val pickerLayout = layout.findViewById<LinearLayout>(R.id.date_time_picker)
        val datePicker = layout.findViewById<DatePicker>(R.id.datePicker)
        val timePicker = layout.findViewById<TimePicker>(R.id.timePicker)


        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val layout = findViewById<LinearLayout>(R.id.date_time_picker)
            if (isChecked == true)
                pickerLayout.visibility = View.VISIBLE

            else
                pickerLayout.visibility = View.GONE
        }

        val notificationUtils = NotificationUtils()

        builder.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->
            val text = taskNamelayout.text.toString().trim()
            val cat = categoryNamelayout.text.toString().trim()

            if(text.isEmpty() || cat.isEmpty())
            {
                Toast.makeText(this, "Strings are empty", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            else {

                val task = Task(text = text, category = cat)
                if (checkBox.isChecked == true) {
                    task.isAlarmSet = true
                    val time: Long = getTime(timePicker, datePicker)
                    notificationUtils.scheduleNotification(task, this@MainActivity, time)
                }

                viewModel.insert(task)
            }

        })

        builder.setCancelable(false)

        builder.setNeutralButton("Discard", DialogInterface.OnClickListener{ dialog, which ->
            dialog.dismiss()
        })

        val alertDialog = builder.create()
        alertDialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemClicked(task: Task, ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit the task")
        val layout = layoutInflater.inflate(R.layout.alert_dialog, null)
        builder.setView(layout)
        val taskNamelayout = layout.findViewById<TextInputEditText>(R.id.titleEt)
        val categoryNamelayout = layout.findViewById<TextInputEditText>(R.id.categoryEt)
        val checkBox = layout.findViewById<CheckBox>(R.id.setNotification)
        val pickerLayout = layout.findViewById<LinearLayout>(R.id.date_time_picker)
        val datePicker = layout.findViewById<DatePicker>(R.id.datePicker)
        val timePicker = layout.findViewById<TimePicker>(R.id.timePicker)

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val layout = findViewById<LinearLayout>(R.id.date_time_picker)
            if (isChecked == true)
                pickerLayout.visibility = View.VISIBLE

            else
                pickerLayout.visibility = View.GONE
        }

        val notificationUtils = NotificationUtils()

        taskNamelayout.setText(task.text)
        categoryNamelayout.setText(task.category)

        builder.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->
            val text = taskNamelayout.text.toString().trim()
            val cat = categoryNamelayout.text.toString().trim()

            if(text.isEmpty() || cat.isEmpty())
            {
                Toast.makeText(this, "Strings are empty", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            else {

                if (checkBox.isChecked == true) {
                    task.isAlarmSet = true
                    val time: Long = getTime(timePicker, datePicker)
                    notificationUtils.scheduleNotification(task, this@MainActivity, time)
                }

                task.text = taskNamelayout.text.toString()
                task.category = categoryNamelayout.text.toString()
                viewModel.update(task)
            }

        })

        builder.setNeutralButton("Discard", DialogInterface.OnClickListener{ dialog, which ->
            dialog.dismiss()
        })

        builder.setCancelable(false)

        val alertDialog = builder.create()
        alertDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTime(timePicker : TimePicker, datePicker : DatePicker): Long
    {
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }


    fun setUpSwipeGesture(){
        val swipeGesture = object : SwipeGesture(){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                Toast.makeText(this@MainActivity, "Swipe right to delete item", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction){
                    ItemTouchHelper.RIGHT->{
                        val task = adapter.taskslist[viewHolder.adapterPosition]
                        viewModel.delete(task)

                        Toast.makeText(this@MainActivity, "Item deleted", Toast.LENGTH_SHORT).show()
                        Snackbar.make(binding.root, "My message", Snackbar.LENGTH_SHORT)
                            .setAction("Undo", View.OnClickListener {
                                viewModel.insert(task)
                            }).show()
                    }

                }

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.tasksRecyclerView)
    }

}