package com.example.passmeprofessor

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Button


class MainActivity : AppCompatActivity() {

    private lateinit var game: Game

    private lateinit var gestureDetector: GestureDetector
    private var initialX = 450.0F
    private lateinit var paperView: ImageView
    private lateinit var animator: ObjectAnimator
    private val LEFT_SWIPE_THRESH: Float = 200.0f
    private val RIGHT_SWIPE_THRESH: Float = 500.0f

    @SuppressLint("ClickableViewAccessibility")
    private lateinit var originalBitmap: Bitmap
    private fun captureViewBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(android.graphics.Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }

    private fun applyBlurToBackground() {
        val rootView = findViewById<View>(android.R.id.content)
        val blurredBackground = findViewById<ImageView>(R.id.blurred_background)

        originalBitmap = captureViewBitmap(rootView)
        val mainActivityBitmap = captureViewBitmap(rootView)
        val blurredBitmap = BlurBuilder.blur(this, mainActivityBitmap)
        blurredBackground.setImageBitmap(blurredBitmap)
    }

    private fun removeBlurFromBackground() {
        val blurredBackground = findViewById<ImageView>(R.id.blurred_background)
        blurredBackground.setImageBitmap(originalBitmap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the screen to main screen
        setContentView(R.layout.activity_main)
        paperView = findViewById(R.id.paper)
        //Create game instance
        game = Game()
        //Build first paper in game instance with ImageView paper
        game.buildPaper(paperView)
        //Build timer in game instance with TextView timerText and total game time
        game.buildTimer(10, findViewById(R.id.timerText))
        //Build rubric in game instance with rubric text views for A,B,C,D, and E
        game.buildRubric(
            findViewById(R.id.rubric_a),
            findViewById(R.id.rubric_b),
            findViewById(R.id.rubric_c),
            findViewById(R.id.rubric_d),
            findViewById(R.id.rubric_e)
        )

        gestureDetector = GestureDetector(this, object : HorizontalSwipeListener() {
            override fun onSwipeHorizontal(diffX: Float) {
                paperView.x = initialX + diffX
            }
        })
        var blurred = true
        val timerText = findViewById<TextView>(R.id.timerText)
        //var timer =  Timer(95, timerText)

        val blurButton = findViewById<Button>(R.id.blurButton)
        blurButton.setOnClickListener {
            blurred = if(blurred){
                removeBlurFromBackground()
                false
            } else {
                applyBlurToBackground()
                true
            }
        }

        val rootView = findViewById<View>(android.R.id.content)
        rootView.post {
            applyBlurToBackground()
        }


        paperView.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                // Get x position of paper when it is released
                val releasedAt = paperView.x

                if (releasedAt <= LEFT_SWIPE_THRESH) {
                    Log.d("Gestures: ", "LEFT SWIPE DETECTED!")
                    game.fireSwipeEvent(SwipeEvent(this, false))
                } else if (releasedAt >= RIGHT_SWIPE_THRESH) {
                    Log.d("Gestures: ", "RIGHT SWIPE DETECTED!")
                    game.fireSwipeEvent(SwipeEvent(this, true))
                } else {
                    //If user doesn't swipe far enough to register a swipe animate the paper returning to center and don't change player score
                    Log.d("Gestures: ", "NO SWIPE DETECTED!")
                    animateImageViewToInitialPosition()
                    return@setOnTouchListener true
                }
                paperView.visibility = View.INVISIBLE
                Log.d("Released at: ", "$releasedAt")
                paperView.x = initialX
                paperView.visibility = View.VISIBLE
            }
            true
        }

        game.setNewPaper()
    }

    private fun animateImageViewToInitialPosition() {
        animator = ObjectAnimator.ofFloat(paperView, "x", initialX)
        animator.duration = 300
        animator.start()
    }
}
