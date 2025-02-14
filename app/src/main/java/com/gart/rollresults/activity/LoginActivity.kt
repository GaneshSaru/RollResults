package com.gart.rollresults.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gart.rollresults.R
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.content.Intent
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        //Radius
        val cardView = findViewById<FrameLayout>(R.id.cardView)
        val drawable = ContextCompat.getDrawable(this, R.drawable.rounded_top_corner)
        cardView.background = drawable

        // OnClickListener for Register TextView
        val registerlink = findViewById<TextView>(R.id.register)
        registerlink.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // OnClickListener for Login Button
        val login = findViewById<Button>(R.id.login)
        login.setOnClickListener{

        }
    }
}