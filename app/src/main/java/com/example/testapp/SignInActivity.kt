@file:Suppress("DEPRECATION")

package com.example.testapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButtonLayout: FrameLayout
    private lateinit var signInButtonText: TextView
    private lateinit var registerTextView: TextView

    private val SIGN_UP_REQUEST_CODE = 1

    private val TAG = "SignInActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        Log.d(TAG, "вызов onCreate")

        emailEditText = findViewById(R.id.et_email)
        passwordEditText = findViewById(R.id.et_password)
        signInButtonLayout = findViewById(R.id.fl_sign_in_button)
        signInButtonText = findViewById(R.id.tv_sign_in_button)
        registerTextView = findViewById(R.id.tv_register)

        updateSignInButtonState()

        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        signInButtonLayout.setOnClickListener {
            if (signInButtonLayout.isEnabled) {
                val intent = Intent(this, HomeActivity::class.java)
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                startActivity(intent)
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateSignInButtonState()
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun updateSignInButtonState() {
        val isEmailValid = emailEditText.text.length >= 8
        val isPasswordValid = passwordEditText.text.length >= 8

        if (isEmailValid && isPasswordValid) {
            signInButtonLayout.findViewById<View>(R.id.view_sign_in_background)
                .setBackgroundResource(R.drawable.rounded_rectangle)
            signInButtonLayout.isEnabled = true
        } else {
            signInButtonLayout.findViewById<View>(R.id.view_sign_in_background)
                .setBackgroundResource(R.drawable.rounded_rectangle_gray)
            signInButtonLayout.isEnabled = false
        }

        registerTextView.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(signUpIntent, SIGN_UP_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_UP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                // Вариант 1: Получение данных в виде String
                val username = it.getStringExtra("username")
                val email = it.getStringExtra("email")
                val password = it.getStringExtra("password")

                // Вариант 2: Получение объекта User
                val user = it.getSerializableExtra("user") as? User

                // Отображение данных в UI или Logcat
                username?.let { Log.d(TAG, "Username: $username") }
                email?.let { Log.d(TAG, "Email: $email") }
                password?.let { Log.d(TAG, "Password: $password") }
                user?.let { Log.d(TAG, "User object - Username: ${user.username}, Email: ${user.email}, Password: ${user.password}") }
            }
        }
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