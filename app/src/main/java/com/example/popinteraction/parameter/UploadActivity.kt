package com.example.popinteraction.parameter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.R

class UploadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
    }

    fun navigateToParameter(view: View) {
        val intent = Intent(this, ParameterActivity::class.java)
        startActivity(intent)
    }

    fun popupUpload(view: View) {
        val intent = Intent(this, UploadPopup::class.java)
        startActivity(intent)
    }
}