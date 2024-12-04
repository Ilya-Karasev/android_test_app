package com.example.testapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.Serializable

class SignUpActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var returnTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        usernameEditText = findViewById(R.id.et_username)
        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.et_password)
        registerButton = findViewById(R.id.btn_register)
        returnTextView = findViewById(R.id.tv_return)

        usernameEditText.addTextChangedListener(textWatcher)
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Вариант 1: Передача данных в виде String
            val resultIntent = Intent().apply {
                putExtra("username", username)
                putExtra("email", email)
                putExtra("password", password)
            }

            // Вариант 2: Передача объекта User
            val user = User(username, email, password)
            resultIntent.putExtra("user", user)

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        returnTextView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        updateRegisterButtonState()
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateRegisterButtonState()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun updateRegisterButtonState() {
        val isUsernameValid = usernameEditText.text.length >= 8
        val isEmailValid = emailEditText.text.length >= 8
        val isPasswordValid = passwordEditText.text.length >= 8

        if (isUsernameValid && isEmailValid && isPasswordValid) {
            registerButton.isEnabled = true
            registerButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple)) // Цветная кнопка
        } else {
            registerButton.isEnabled = false
            registerButton.setBackgroundColor(ContextCompat.getColor(this, R.color.gray)) // Серый цвет
        }
    }
}

//data class User(val username: String, val email: String, val password: String) : Serializable