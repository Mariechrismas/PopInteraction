package com.example.popinteraction.parameter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.MainActivity
import com.example.popinteraction.R

class ParameterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameter)
    }

    fun navigateToMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun navigateToUpload(view: View) {
        val intent = Intent(this, UploadActivity::class.java)
        startActivity(intent)
    }

    fun navigateToCategory(view: View) {
        val intent = Intent(this, CategoriyActivity::class.java)
        startActivity(intent)
    }
}