package com.example.passmeprofessor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.View
import android.os.CountDownTimer
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timerText = findViewById<TextView>(R.id.timerText)

        //val btn = findViewById<Button>(R.id.button)


        var timer =  Timer(95, timerText)

    }


}