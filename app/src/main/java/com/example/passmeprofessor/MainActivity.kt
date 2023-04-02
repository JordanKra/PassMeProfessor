package com.example.passmeprofessor

import android.R.attr.button
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import android.os.CountDownTimer
import android.widget.Button

class MainActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_main)
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


        var timer = Timer(95, timerText)
        val paperSprite = Paper(findViewById<ImageView>(R.id.paper))
        paperSprite.generateRandomPaper()
    }
}