 package com.gart.rollresults.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.gart.rollresults.R
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifImageView

 class IntroActivity : AppCompatActivity() {

     private lateinit var auth: FirebaseAuth
     private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Write a message to the database
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")

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
