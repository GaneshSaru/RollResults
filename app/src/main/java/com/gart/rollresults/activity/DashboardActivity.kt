package com.gart.rollresults.activity

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.gart.rollresults.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.gart.rollresults.RetrofitClient
import com.gart.rollresults.RollNoRequest
import com.gart.rollresults.database.DatabaseHelper
import com.gart.rollresults.databinding.ActivityDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var dbHelper: DatabaseHelper
    private var loggedInUserId: Int = -1  // Store dynamically fetched user ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // Fetch user ID from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        loggedInUserId = sharedPreferences.getInt("user_id", -1)

        if (loggedInUserId == -1) {
            Toast.makeText(this, "Error: No logged-in user found!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupSemesterSpinner()
        displayUserInfo()

        binding.findit.setOnClickListener {
            val selectedSemester = binding.semesterSpinner.selectedItem?.toString()

            if (selectedSemester == "Select Semester" || selectedSemester.isNullOrEmpty()) {
                Toast.makeText(this, "Please select a semester", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fetchResult(getLoggedInUserRollNo(), selectedSemester)
        }
    }

    private fun setupSemesterSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.semesters,
            android.R.layout.simple_spinner_item
        )

        // Customizing the dropdown text
        val typeface = ResourcesCompat.getFont(this, R.font.quicksandmedium)
        val spinnerTextView = TextView(this)
        spinnerTextView.typeface = typeface

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.semesterSpinner.adapter = adapter
    }

    private fun fetchResult(rollNo: String, semester: String) {
        binding.progressBar.visibility = View.VISIBLE

        RetrofitClient.instance.getResult(RollNoRequest(rollNo, semester))
            .enqueue(object : Callback<Map<String, String>> {
                override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                    binding.progressBar.visibility = View.GONE
                    binding.resultTableLayout.removeAllViews()

                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()!!

                        result.entries.forEach { (key, value) ->
                            if (key != "Exam Roll No." && key != "SGPA") {
                                addTableRow(key, value)
                            }
                        }
                        // Add SGPA (whether valid or has "-" value) only once
                        val sgpaValue = result["SGPA"]
                        addTableRow("SGPA", sgpaValue ?: "-") // If SGPA is null, use "-" as default

                        binding.scrollView.visibility = View.VISIBLE
                    } else {
                        addTableRow("Error", "Roll number not found.")
                        binding.scrollView.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.resultTableLayout.removeAllViews()
                    addTableRow("Error", "Error: ${t.message}")
                    binding.scrollView.visibility = View.VISIBLE
                }
            })
    }

    private fun addTableRow(key: String, value: String) {
        val tableRow = TableRow(this)
        val keyTextView = TextView(this).apply {
            text = key
            setPadding(16, 16, 16, 16)
            gravity = Gravity.START

            val typeface = ResourcesCompat.getFont(context, R.font.quicksandbold)
            setTypeface(typeface)
        }
        val valueTextView = TextView(this).apply {
            text = value
            setPadding(16, 16, 16, 16)
            gravity = Gravity.END

            val typeface = ResourcesCompat.getFont(context, R.font.quicksandbold)
            setTypeface(typeface)
        }
        tableRow.addView(keyTextView)
        tableRow.addView(valueTextView)
        binding.resultTableLayout.addView(tableRow)
    }

    private fun getLoggedInUserRollNo(): String {
        val user = dbHelper.getUserDetails(loggedInUserId) // ✅ Now using getUserDetails()
        return user?.examRollNo ?: "" // Return empty string if user not found
    }


    private fun displayUserInfo() {
        val user = dbHelper.getUserDetails(loggedInUserId) // ✅ Now using getUserDetails()

        if (user != null) {
            binding.apply {
                userFullName.text = "Name: ${user.fullName}"
                examRollno.text = "Exam Roll No.: ${user.examRollNo}"
                userCollege.text = "College: ${user.college}"
                userProgram.text = "Program: ${user.program}"
            }
        } else {
            Toast.makeText(this, "Error: User not found!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

}
