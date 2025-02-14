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
import android.widget.Toast
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // For UI
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

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val emailEditText: TextInputEditText = findViewById(R.id.email)
        val passwordEditText: TextInputEditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login)

        loginButton.setOnClickListener{
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if(email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and Passowrd", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginUser(email, password)
        }
    }

    private fun loginUser(email:String, password:String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                    // Navigate to Dashboard
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish() // Close login activity
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
    }
}