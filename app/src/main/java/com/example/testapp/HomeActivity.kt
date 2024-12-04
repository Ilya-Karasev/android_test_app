package com.example.testapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var logoutButton: ImageButton
    private lateinit var searchButton: ImageButton
    private lateinit var profileButton: ImageButton

    private val TAG = "HomeActivity"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Log.d(TAG, "вызов onCreate")

        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        chatRecyclerView = findViewById(R.id.rv_chat_list)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = ChatList(getChatList())

        logoutButton = findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        searchButton = findViewById(R.id.btn_search)

        profileButton = findViewById(R.id.btn_profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }
    }

    private fun getChatList(): List<Chat> {
        return listOf(
            Chat("Алексей Иванов", "Привет, как дела?", "15:30", R.drawable.profile_image),
            Chat("Мария Петрова", "Давно не виделись!", "14:50", R.drawable.profile_image),
        )
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "вызов onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "вызов onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "вызов onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "вызов onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "вызов onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "вызов onRestart")
    }
}