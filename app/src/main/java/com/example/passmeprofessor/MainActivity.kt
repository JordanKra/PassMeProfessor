package com.example.passmeprofessor

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var game: Game
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the screen to main screen
        setContentView(R.layout.activity_main)
        //Create game instance
        game = Game()
        //Build timer in game instance with TextView timerText and total game time
        game.buildTimer(10, findViewById(R.id.timerText))
        //Build first paper in game instance with ImageView paper
        game.buildPaper(findViewById(R.id.paper))
        //paperSprite.generateRandomPaper()

    }



}