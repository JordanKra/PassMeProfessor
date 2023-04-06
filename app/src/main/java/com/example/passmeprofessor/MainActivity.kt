package com.example.passmeprofessor

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var game: Game

    private lateinit var gestureDetector: GestureDetector
    private var initialX = 250.0F
    private lateinit var paperView: ImageView

    @SuppressLint("ClickableViewAccessibility")
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

        paperView.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            if (motionEvent.action == MotionEvent.ACTION_UP) {
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
