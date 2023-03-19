package com.example.passmeprofessor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.view.View
import android.os.CountDownTimer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timerText = findViewById<TextView>(R.id.timerText)

        object : CountDownTimer(65000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var seconds = millisUntilFinished/1000/60
                if(seconds == 0L){
                    seconds = millisUntilFinished/1000
                } else {
                    seconds = millisUntilFinished / 60/ 1000
                }
                timerText.setText("" + millisUntilFinished/1000/60 + ":" + seconds)
            }

            override fun onFinish() {
                timerText.setText("done!")
            }
        }.start()

    }


}