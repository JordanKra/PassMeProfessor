package com.example.passmeprofessor

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import kotlin.properties.Delegates


private const val DEBUG_TAG = "Gestures"

class MainActivity : AppCompatActivity() {

    private lateinit var game: Game

    private lateinit var mDetector: GestureDetectorCompat
    private lateinit var gestureDetector: GestureDetector
    private var initialX = 250.0F
    private lateinit var paperView: ImageView

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
        paperView = findViewById(R.id.paper)
        //initialX = paperView.x
        //paperSprite.generateRandomPaper()


        gestureDetector = GestureDetector(this, object : HorizontalSwipeListener() {
            override fun onSwipeHorizontal(diffX: Float) {
                paperView.x = initialX + diffX
            }
        })

        paperView.setOnTouchListener {_, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            if(motionEvent.action == MotionEvent.ACTION_UP){
                animateImageViewToInitialPosition()
            }
            true
        }

        game.setNewPaper()
    }

    private fun animateImageViewToInitialPosition() {
        val animator = ObjectAnimator.ofFloat(paperView, "x", initialX)
        animator.duration = 300
        animator.start()
    }
}
    /*override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }*/

private class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VEL_THRESH = 100
    override fun onDown(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDown: $event")
        return true
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        val diffx = (e2?.x ?: 0.0F) - (e1?.x ?: 0.0F)
        return true
    }

    fun onSwipeHorizontal(diffx: Float){
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
}
