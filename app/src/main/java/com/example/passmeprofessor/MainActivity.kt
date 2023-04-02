package com.example.passmeprofessor

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timerText = findViewById<TextView>(R.id.timerText)

        var timer = Timer(95, timerText)
        val paperSprite = Paper(findViewById<ImageView>(R.id.paper))
        paperSprite.generateRandomPaper()
    }
}