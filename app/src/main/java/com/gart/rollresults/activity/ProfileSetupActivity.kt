package com.gart.rollresults.activity

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gart.rollresults.R
import android.widget.Toast
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileSetupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_setup)

        // For UI
        // Radius
        val cardView: FrameLayout = findViewById(R.id.cardView)
        val drawable = ContextCompat.getDrawable(this, R.drawable.rounded_top_corner)
        cardView.background = drawable

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirerestore.getInstance()

        val fullNameEditText: TextInputEditText = findViewById(R.id.fullName)
        val examRollNoEditText: TextInputEditText = findViewById(R.id.examRollno)
        val collegeEditText: TextInputEditText = findViewById(R.id.college)
        val programEditText: TextInputEditText = findViewById(R.id.program)
        val regCompleteButton: Button = findViewById(R.id.regComplete)

        val email = intent.getStringExtra("email")?: ""
        val password = intent.getStringExtra("password")?: ""

        regCompleteButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString().trim()
            val examRollNo = examRollNoEditText.text.toString().trim()
            val college = collegeEditText.text.toString().trim()
            val program = programEditText.text.toString().trim()

            if (fullName.isEmpty() || examRollNo.isEmpty() || college.isEmpty() || program.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create Firebase account and store user data
            createAccount (email, password, fullName, examRollNo, college, program)
        }
    }

    private fun createAccount(email: String, password: String, fullName: String, examRollno: String, college: String, program: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userMap = hashMapOf(
                        "fullName" to fullName,
                        "examRollno" to examRollno
                        "college" to college,
                        "program" to program,
                        "email" to email
                    )

                    // Store user details in Firestore
                    db.collection("users").document(userId).set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registration Complete!", Toast.LENGTH_SHORT).show()

                            // Navigate to login Activity
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"Failed to save user data!", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}",Toast.LENGTH_SHORT).show()
                }
            }
    }
}
