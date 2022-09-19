package com.bignerdranch.android.lab11json

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var nameTask: EditText
    private lateinit var namesTask: EditText
    private lateinit var textTask: EditText
    private lateinit var dateTask: CalendarView
    private lateinit var btnTask: Button
    private lateinit var btnTaskInfo: ImageButton
    private lateinit var tvHead: TextView
    private lateinit var listTask: MutableList<TaskClass?>

    private val APP = "prefs"
    private lateinit var prefs: SharedPreferences

    private var date : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        nameTask = findViewById(R.id.nameTask)
        namesTask = findViewById(R.id.namesTask)
        textTask = findViewById(R.id.textTask)
        btnTask = findViewById(R.id.buttonTask)
        dateTask = findViewById(R.id.calendarView)
        btnTaskInfo = findViewById(R.id.backTaskInfo)
        tvHead = findViewById(R.id.textView3)

        var index = intent.getIntExtra("index",-1)
        prefs = getSharedPreferences(APP, Context.MODE_PRIVATE)
        if(prefs.contains("JSON_STRING")){
            val json:String? = prefs.getString("JSON_STRING", "")
            listTask = Gson().fromJson<MutableList<TaskClass>>(json, object: TypeToken<MutableList<TaskClass>>() {}.type)
                .toMutableList()
            listTask.forEach(){
                Log.d("JSON-INFO", it.toString())
            }

        }



        dateTask.setOnDateChangeListener(){view, year, month, dayOfMonth -> date = "$dayOfMonth.${month+1}.$year"}
        val cl = Calendar.getInstance()

        if(index > -1){
            val date = listTask[index]?.date?.split(".")
            cl.set(date?.get(2)!!.toInt(),date[1].toInt()-1,date[0].toInt())
            tvHead.text = "Редактирование"
            btnTask.text = "Изменить"
            nameTask.setText(listTask[index]?._name.toString())
            namesTask.setText(listTask[index]?.names.toString())
            textTask.setText(listTask[index]?._text.toString())
            dateTask.date = cl.timeInMillis
        }

        //Отправка
        btnTask.setOnClickListener {
            val int = Intent(this, TaskInfo::class.java)
            if(index == -1){
                val task = Gson().toJson(TaskClass(nameTask.text.toString(), textTask.text.toString(),namesTask.text.toString(), date.toString()))
                int.putExtra("JSONs", task)
            }
            else
            {
                listTask[index]?._name = nameTask.text.toString()
                listTask[index]?.names = namesTask.text.toString()
                listTask[index]?._text = textTask.text.toString()
                listTask[index]?.date = date.toString()

                val JSON = Gson().toJson(listTask).toString()
                val edit = prefs.edit()
                edit.putString("JSON_STRING", JSON)
                edit.apply()
            }
            super.onBackPressed()
            //startActivity(int)
        }
        btnTaskInfo.setOnClickListener {
            super.onBackPressed()
        }

    }



}