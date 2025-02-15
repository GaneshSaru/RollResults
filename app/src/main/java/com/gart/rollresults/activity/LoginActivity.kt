package com.gart.rollresults.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.gart.rollresults.R
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import android.widget.Button
import com.gart.rollresults.database.DatabaseHelper
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Set rounded corners
        val cardView = findViewById<FrameLayout>(R.id.cardView)
        val drawable = ContextCompat.getDrawable(this, R.drawable.rounded_top_corner)
        cardView.background = drawable

        // Register link click listener
        val registerLink = findViewById<TextView>(R.id.register)
        registerLink.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        // Initialize DatabaseHelper and SharedPreferences
        dbHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // UI elements
        val emailEditText: TextInputEditText = findViewById(R.id.email)
        val passwordEditText: TextInputEditText = findViewById(R.id.password)
        val loginButton: Button = findViewById(R.id.login)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else{
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val userId = dbHelper.authenticateUser(email, password)

        Log.d("DEBUG", "User ID returned: $userId") // Debugging output

        if (userId != -1) {
            // Store user ID in SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putInt("user_id", userId)
            editor.apply()

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

            // Navigate to Dashboard
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish() // Close login activity
        } else {
            Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show()
        }
    }
}
