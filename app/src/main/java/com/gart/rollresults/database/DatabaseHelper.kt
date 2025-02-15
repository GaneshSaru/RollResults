package com.gart.rollresults.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "rollresults.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"

        // Columns
        const val COLUMN_ID = "id"
        const val COLUMN_FULL_NAME = "full_name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_EXAM_ROLL_NO = "exam_roll_no"
        const val COLUMN_COLLEGE = "college"
        const val COLUMN_PROGRAM = "program"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FULL_NAME TEXT,
                $COLUMN_EMAIL TEXT UNIQUE,
                $COLUMN_PASSWORD TEXT,
                $COLUMN_EXAM_ROLL_NO TEXT,
                $COLUMN_COLLEGE TEXT,
                $COLUMN_PROGRAM TEXT
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Insert user data
    fun insertUser(fullName: String, email: String, password: String, examRollNo: String, college: String, program: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FULL_NAME, fullName)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_EXAM_ROLL_NO, examRollNo)
            put(COLUMN_COLLEGE, college)
            put(COLUMN_PROGRAM, program)
        }
        val result = db.insert(TABLE_USERS, null, values)
        return result != -1L
    }

    // Check if user exists for login
    fun authenticateUser(email: String, password: String): Int {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        var userId = -1  // Default value if no user is found
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0) // Retrieve unique user ID
        }

        Log.d("DEBUG", "authenticateUser result: $userId") // Debugging output

        cursor.close()
        return userId // Return true if user is found, false otherwise
    }

    // Get user details by user ID
    fun getUserDetails(userId: Int): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_ID = ?", arrayOf(userId.toString()))
        var user: User? = null

        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FULL_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                examRollNo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EXAM_ROLL_NO)),
                college = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLLEGE)),
                program = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROGRAM))
            )
        }

        cursor.close()
        return user
    }
}

data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val examRollNo: String,
    val college: String,
    val program: String
)

   /*
    // **Get user details by user ID**
    fun getUserId(email: String, password: String): Int {
        val db = readableDatabase
        val query = "SELECT id FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        var userId = -1

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        }

        cursor.close()
        return userId
    }
}
data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val examRollNo: String,
    val college: String,
    val program: String
)


    */