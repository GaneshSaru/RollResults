 package com.gart.rollresults.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gart.rollresults.R
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView
import android.os.Handler
import android.os.Looper
import android.content.Intent


class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Gif ImageView Animation
        val gifImageView = findViewById<GifImageView>(R.id.gifImageView)

        try {
            val gifDrawable = GifDrawable(resources, R.drawable.logoanimation)
            gifDrawable.loopCount = 1  // Play only once
            gifImageView.setImageDrawable(gifDrawable)

            // Get the duration of the GIF animation
            val gifDuration = 5000  // 5000 milliseconds = 5secs

            // Delay transition to LoginActivity until the animation is finished
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // close IntroActivity
            }, gifDuration.toLong())

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
