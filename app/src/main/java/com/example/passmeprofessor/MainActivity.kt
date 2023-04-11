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
import android.widget.Button


class MainActivity : AppCompatActivity(), TimerEndListener{

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

    private fun makeAllInvisible(){
        findViewById<View>(R.id.rubric_a).visibility =  View.INVISIBLE
        findViewById<View>(R.id.rubric_b).visibility =  View.INVISIBLE
        findViewById<View>(R.id.rubric_c).visibility =  View.INVISIBLE
        findViewById<View>(R.id.rubric_d).visibility =  View.INVISIBLE
        findViewById<View>(R.id.rubric_e).visibility =  View.INVISIBLE
        findViewById<View>(R.id.timerText).visibility =  View.INVISIBLE
        findViewById<View>(R.id.scoreText).visibility =  View.INVISIBLE
        findViewById<View>(R.id.rubric).visibility =  View.INVISIBLE
        paperView.visibility = View.INVISIBLE
    }

    private fun makeAllVisible(){
        findViewById<View>(R.id.rubric_a).visibility =  View.VISIBLE
        findViewById<View>(R.id.rubric_b).visibility =  View.VISIBLE
        findViewById<View>(R.id.rubric_c).visibility =  View.VISIBLE
        findViewById<View>(R.id.rubric_d).visibility =  View.VISIBLE
        findViewById<View>(R.id.rubric_e).visibility =  View.VISIBLE
        findViewById<View>(R.id.timerText).visibility =  View.VISIBLE
        findViewById<View>(R.id.scoreText).visibility =  View.VISIBLE
        findViewById<View>(R.id.rubric).visibility =  View.VISIBLE
        paperView.visibility = View.VISIBLE
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the screen to main screen
        setContentView(R.layout.activity_main)
        paperView = findViewById(R.id.paper)
        //Create game instance
        game = Game()
        game.addTimerEndEventListener(this)
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
        game.findScoreText(findViewById(R.id.scoreText))
        makeAllInvisible()
        gestureDetector = GestureDetector(this, object : HorizontalSwipeListener() {
            override fun onSwipeHorizontal(diffX: Float) {
                paperView.x = initialX + diffX
            }
        })

        val blurButton = findViewById<Button>(R.id.blurButton)
        blurButton.setOnClickListener {
                blurButton.isEnabled = false
                blurButton.visibility = View.INVISIBLE
                removeBlurFromBackground()
                makeAllVisible()
                game.start();
        }

        val rootView = findViewById<View>(android.R.id.content)
        rootView.post {
            blurButton.alpha = 0f
            applyBlurToBackground()
            blurButton.alpha = 1f
        }

        paperView.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            if (motionEvent.action == MotionEvent.ACTION_UP && game.started) {
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
                //paperView.visibility = View.INVISIBLE
                Log.d("Released at: ", "$releasedAt")
                paperView.x = initialX
                //paperView.visibility = View.VISIBLE
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

    override fun onTimerEnd(event: TimerEndEvent?) {
        makeAllInvisible()
        applyBlurToBackground()
        val blurButton = findViewById<Button>(R.id.blurButton)
        blurButton.text = "Try Again"
        blurButton.isEnabled = true
        blurButton.visibility = View.VISIBLE
    }
}
