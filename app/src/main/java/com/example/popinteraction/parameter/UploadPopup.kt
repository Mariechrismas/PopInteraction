package com.example.popinteraction.parameter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Xml
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Spinner
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.popinteraction.R
import org.xmlpull.v1.XmlSerializer
import java.io.StringWriter

class UploadPopup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_upload)
    }

    fun navigateToUpload(view: View) {
        val intent = Intent(this, UploadActivity::class.java)
        startActivity(intent)
    }

}