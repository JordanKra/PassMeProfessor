package com.example.passmeprofessor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timerText = findViewById<TextView>(R.id.timerText)

        var timer =  Timer(95, timerText)
        var paper_sprite = Paper(findViewById<ImageView>(R.id.paper))
        paper_sprite.generateRandomPaper()
    }
}