package com.bignerdranch.android.lab11json

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TaskInfo : AppCompatActivity() {

    private lateinit var addTask : ImageButton
    private lateinit var listTask: MutableList<TaskClass?>

    private val APP = "prefs"
    private lateinit var prefs: SharedPreferences

    private var index : Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_info)
        listTask = mutableListOf()
        addTask = findViewById(R.id.addTaskBtn)

        //Подгрузка задач

        //UpdateTaskInfo()
        prefs = getSharedPreferences(APP, Context.MODE_PRIVATE)
        if(prefs.contains("JSON_STRING")){
            val json:String? = prefs.getString("JSON_STRING", "")
            listTask = Gson().fromJson<MutableList<TaskClass>>(json, object: TypeToken<MutableList<TaskClass>>() {}.type)
                .toMutableList()
            listTask.forEach(){
                Log.d("JSON-INFO", it.toString())
            }

        }

        var json = intent.getStringExtra("JSONs").toString()
        val task = Gson().fromJson<TaskClass>(json, object: TypeToken<TaskClass>() {}.type) ?: null
        if(task != null){
            listTask.add(task)
        }

        //Переход в окно добавления
        addTask.setOnClickListener {
            val reDir = Intent(this, MainActivity::class.java)
            startActivity(reDir)
        }
        val rv = findViewById<RecyclerView>(R.id.TaskLisrRv)
        val adapter = TaskRVAdapter(this, listTask)
        val rvListener = object : TaskRVAdapter.ItemClickListener{
            override fun onItemClick(view: View?, position: Int) {
                val intent = Intent(this@TaskInfo, MainActivity::class.java)
                intent.putExtra("index", position)
                index = position
                startActivity(intent)
            }
        }
        adapter.setClickListener(rvListener)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

    }

    override fun onStop() {
        super.onStop()
        val JSON = Gson().toJson(listTask).toString()
        val edit = prefs.edit()
        edit.putString("JSON_STRING", JSON)
        edit.apply()

    }
    override fun onBackPressed() {
        //super.onBackPressed()
        AlertDialog.Builder(this).apply {
            setTitle("Подтверждение")
            setMessage("Вы уверены, что хотите выйти из программы?")
            setPositiveButton("Да"){ _, _ ->
                super.onBackPressed()
            }
            setNegativeButton("Нет"){ _, _ ->

            }
            setCancelable(true)
        }.create().show()
    }

    override fun onResume() {
        super.onResume()
        if(index != -1){
            listTask.clear()
            UpdateTaskInfo()
        }
        prefs = getSharedPreferences(APP, Context.MODE_PRIVATE)
        if(prefs.contains("JSON_STRING")){
            val json:String? = prefs.getString("JSON_STRING", "")
            listTask = Gson().fromJson<MutableList<TaskClass>>(json, object: TypeToken<MutableList<TaskClass>>() {}.type)
                .toMutableList()
            listTask.forEach(){
                Log.d("JSON-INFO", it.toString())
            }

        }

        var json = intent.getStringExtra("JSONs").toString()
        val task = Gson().fromJson<TaskClass>(json, object: TypeToken<TaskClass>() {}.type) ?: null
        if(task != null){
            listTask.add(task)
        }

    }

    override fun onStart() {
        super.onStart()
    }
    fun UpdateTaskInfo(){
        prefs = getSharedPreferences(APP, Context.MODE_PRIVATE)
        if(prefs.contains("JSON_STRING")){
            val json:String? = prefs.getString("JSON_STRING", "")
            listTask = Gson().fromJson<MutableList<TaskClass>>(json, object: TypeToken<MutableList<TaskClass>>() {}.type)
                .toMutableList()
            listTask.forEach(){
                Log.d("JSON-INFO", it.toString())
            }

        }

        var json = intent.getStringExtra("JSONs").toString()
        val task = Gson().fromJson<TaskClass>(json, object: TypeToken<TaskClass>() {}.type) ?: null
        if(task != null){
            listTask.add(task)
        }
    }
}