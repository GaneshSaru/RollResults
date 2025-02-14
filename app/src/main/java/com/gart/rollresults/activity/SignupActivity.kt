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
import android.widget.Toast
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.android.material.textfield.TextInputEditText


class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        //For UI
        // Radius
        val cardView: FrameLayout = findViewById(R.id.cardView)
        val drawable = ContextCompat.getDrawable(this, R.drawable.rounded_top_corner)
        cardView.background = drawable

        // OnClickListener for Register TextView
        val loginlink = findViewById<TextView>(R.id.login)
        loginlink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // OnClickListener for Login Button

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val emailEditText: TextInputEditText = findViewById(R.id.email)
        val passwordEditText: TextInputEditText = findViewById(R.id.password)
        val confirmPasswordEditText: TextInputEditText = findViewById(R.id.confirmPassword)
        val signupButton: Button = findViewById(R.id.sign_up)

        signupButton.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password == confirmPassword) {
                // Naviaget to ProfileSetupActivity with email & password to complete registration
                val intent = Intent(this,ProfileSetupActivity::class.java)
                intent.putExtra("email",email)
                intent.putExtra("password", password)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Passwrods do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
