package com.gart.rollresults.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.gart.rollresults.R
import android.content.Intent
import android.widget.Toast
import android.widget.Button
import com.gart.rollresults.database.DatabaseHelper
import com.google.android.material.textfield.TextInputEditText


class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_setup)

        // For UI
        // Radius
        val cardView: FrameLayout = findViewById(R.id.cardView)
        val drawable = ContextCompat.getDrawable(this, R.drawable.rounded_top_corner)
        cardView.background = drawable

        // Initilize db
        dbHelper = DatabaseHelper(this)

        // onClick Listener
        val fullNameEditText: TextInputEditText = findViewById(R.id.fullName)
        val examRollNoEditText: TextInputEditText = findViewById(R.id.examRollno)
        val collegeEditText: TextInputEditText = findViewById(R.id.college)
        val programEditText: TextInputEditText = findViewById(R.id.program)
        val regCompleteButton: Button = findViewById(R.id.regComplete)

        val email = intent.getStringExtra("email")?: ""
        val password = intent.getStringExtra("password")?: ""

        regCompleteButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val examRollno = examRollNoEditText.text.toString().trim()
            val college = collegeEditText.text.toString().trim()
            val program = programEditText.text.toString().trim()

            if (fullName.isEmpty() || examRollno.isEmpty() || college.isEmpty() || program.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = dbHelper.insertUser(fullName, email, password, examRollno, college, program)
            if (success) {
                Toast.makeText(this, "Registration Complete!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
