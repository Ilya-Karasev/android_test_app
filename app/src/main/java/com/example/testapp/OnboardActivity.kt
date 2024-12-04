package com.example.testapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class OnboardActivity : AppCompatActivity() {

    private val TAG = "OnboardActivity"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard)

        Log.d(TAG, "вызов onCreate")

        val continueView = findViewById<View>(R.id.tv_return)
        continueView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
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