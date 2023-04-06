package com.example.passmeprofessor

import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat


private const val DEBUG_TAG = "Gestures"

class MainActivity : AppCompatActivity() {

    private lateinit var game: Game

    private lateinit var mDetector: GestureDetectorCompat

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

        mDetector = GestureDetectorCompat(this, MyGestureListener())

        var timer = Timer(95, timerText)
        val paperSprite = Paper(findViewById<ImageView>(R.id.paper))
        paperSprite.generateRandomPaper()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

}
private class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VEL_THRESH = 100
    override fun onDown(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDown: $event")
        return true
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d(DEBUG_TAG, "onFling: $event1 $event2 $velocityX $velocityY")
        val xdiff = Math.abs(event1.x.minus(event2.x))
        val ydiff = Math.abs(event1.y.minus(event2.y))
        return true

    }

    private fun onLeftSwipe(){

    }

    private fun onRightSwipe(){

    }
}
