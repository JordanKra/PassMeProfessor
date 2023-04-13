package com.example.passmeprofessor

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity(), TimerEndListener{

    private lateinit var game: Game
    private lateinit var gestureDetector: GestureDetector
    private var initialX = 450.0F
    private var mDownX = 0f
    private lateinit var paperView: ImageView
    private lateinit var animator: ObjectAnimator
    private val LEFT_SWIPE_THRESH: Float = 250.0f
    private val RIGHT_SWIPE_THRESH: Float = 650.0f
    var START_GAME_TIME = 30
    private var mSwipeSlop = 0
    private var mSwiping = false
    private var mViewParent: ViewGroup? = null


    @SuppressLint("ClickableViewAccessibility")
    private lateinit var originalBitmap: Bitmap

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the screen to main screen
        setContentView(R.layout.activity_main)
        mSwipeSlop = ViewConfiguration.get(this).getScaledTouchSlop()
        mViewParent = findViewById<ViewGroup?>(android.R.id.content).rootView as ViewGroup?
        paperView = findViewById(R.id.paper)
        //Create game instance
        game = Game()
        game.addTimerEndEventListener(this)
        //Build first paper in game instance with ImageView paper
        game.buildPaper(paperView)
        //Build timer in game instance with TextView timerText and total game time
        game.buildTimer(START_GAME_TIME, findViewById(R.id.timerText))
        //Build rubric in game instance with rubric text views for A,B,C,D, and E
        game.buildRubric(
            findViewById(R.id.rubric_a),
            findViewById(R.id.rubric_b),
            findViewById(R.id.rubric_c),
            findViewById(R.id.rubric_d),
            findViewById(R.id.rubric_e),
            findViewById(R.id.rubric)
        )
        game.findScoreText(findViewById(R.id.scoreText))
        makeAllInvisible()

        //make score text invisible
        findViewById<TextView>(R.id.finalScore).visibility =  View.INVISIBLE
        gestureDetector = GestureDetector(this, object : HorizontalSwipeListener() {
            override fun onSwipeHorizontal(diffX: Float) {
                paperView.x = initialX + diffX
            }
        })

        var mainMusic: MediaPlayer? = null
        if(mainMusic == null) {
            mainMusic = MediaPlayer.create(this, R.raw.fatrat)
            if (mainMusic != null) {
                mainMusic.isLooping = true
            }
            if (mainMusic != null) {
                mainMusic.start()
            }
        }

        val blurButton = findViewById<Button>(R.id.blurButton)
        blurButton.setOnClickListener {
                blurButton.isEnabled = false
                blurButton.visibility = View.INVISIBLE
                removeBlurFromBackground()
               // findViewById<TextView>(R.id.finalScore).visibility =  View.INVISIBLE
                makeAllVisible()
                game.start()
        }

        val rootView = findViewById<View>(android.R.id.content)
        rootView.post {
            blurButton.alpha = 0f
            applyBlurToBackground()
            blurButton.alpha = 1f
        }

        mSwipeSlop = ViewConfiguration.get(this).scaledTouchSlop
        paperView.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            game.rubricSprite.background = null
            paperView.background = null
            if(motionEvent.action == MotionEvent.ACTION_DOWN){
                mDownX = motionEvent.x
                return@setOnTouchListener true
            }
            if(motionEvent.action == MotionEvent.ACTION_MOVE && game.started){
                val currentX = motionEvent.x
                val deltaX = currentX - mDownX
                if(deltaX >= mSwipeSlop){
                    mSwiping = true
                    mViewParent?.requestDisallowInterceptTouchEvent(true)
                }
                return@setOnTouchListener true
            }
            if (motionEvent.action == MotionEvent.ACTION_UP && game.started && mSwiping) {
                // Get x position of paper when it is released
                val releasedAt = paperView.x
                if (releasedAt <= LEFT_SWIPE_THRESH) {
                    Log.d("Gestures: ", "LEFT SWIPE DETECTED! $releasedAt")
                    game.fireSwipeEvent(SwipeEvent(this, false))
                    //Generating new paper handled by fireSwipeEvent()
                } else if (releasedAt >= RIGHT_SWIPE_THRESH) {
                    Log.d("Gestures: ", "RIGHT SWIPE DETECTED! $releasedAt")
                    game.fireSwipeEvent(SwipeEvent(this, true))
                } else {
                    //If user doesn't swipe far enough to register a swipe animate the paper returning to center and don't change player score
                    Log.d("Gestures: ", "NO SWIPE DETECTED! $releasedAt")
                    //game.paperSprite.setBackgroundResource(R.drawable.image_border)
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

    }

    private fun animateImageViewToInitialPosition() {
        animator = ObjectAnimator.ofFloat(paperView, "x", initialX)
        animator.duration = 300
        animator.start()
    }

    override fun onTimerEnd(event: TimerEndEvent?) {
        makeAllInvisible()
        findViewById<TextView>(R.id.finalScore).visibility =  View.INVISIBLE
        applyBlurToBackground()
        val blurButton = findViewById<Button>(R.id.blurButton)
        findViewById<TextView>(R.id.finalScore).setText("Score: " + game.score)
        findViewById<TextView>(R.id.finalScore).visibility =  View.VISIBLE
        blurButton.text = "Try Again"
        blurButton.isEnabled = true
        blurButton.visibility = View.VISIBLE
    }
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
        findViewById<TextView>(R.id.finalScore).visibility =  View.VISIBLE
        findViewById<View>(R.id.leftInstructionText).visibility = View.INVISIBLE
        findViewById<View>(R.id.rightInstructionText).visibility = View.INVISIBLE
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
        findViewById<TextView>(R.id.finalScore).visibility =  View.INVISIBLE
        findViewById<View>(R.id.leftInstructionText).visibility = View.VISIBLE
        findViewById<View>(R.id.rightInstructionText).visibility = View.VISIBLE
        paperView.visibility = View.VISIBLE
    }

}
