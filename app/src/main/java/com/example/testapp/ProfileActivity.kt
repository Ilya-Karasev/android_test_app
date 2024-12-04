package com.example.testapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val email = intent.getStringExtra("email") ?: "example@mail.com"
        val password = intent.getStringExtra("password") ?: "password"

        val maskedPassword = "*".repeat(password.length)

        val emailTextView: TextView = findViewById(R.id.tv_email)
        val passwordTextView: TextView = findViewById(R.id.tv_password)
        emailTextView.text = "Эл.почта: $email"
        passwordTextView.text = "Пароль: $maskedPassword"

        val homeButton: ImageButton = findViewById(R.id.btn_home)
        homeButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}