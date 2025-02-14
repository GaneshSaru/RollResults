package com.gart.rollresults.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gart.rollresults.R

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        // Radius
        val cardView: FrameLayout = findViewById(R.id.cardView)
        val drawable = ContextCompat.getDrawable(this, R.drawable.rounded_top_corner)
        cardView.background = drawable

        // OnClickListener for Register TextView
        val login_link = findViewById<TextView>(R.id.login)
        login_link.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // OnClickListener for Login Button
        val sign_up = findViewById<Button>(R.id.sign_up)
        sign_up.setOnClickListener{

        }
    }
}