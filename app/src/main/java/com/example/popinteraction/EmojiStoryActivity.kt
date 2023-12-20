package com.example.popinteraction

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class EmojiStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emojie_story)
    }

    fun navigateToMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}