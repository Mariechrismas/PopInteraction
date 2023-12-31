package com.example.popinteraction.emojistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.DepixelimageActivity
import com.example.popinteraction.R

class EmojiStoryFrontPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emojie_story_frontpage)

    }

    fun play(view: View){
        val intent = Intent(this, EmojiStoryActivity::class.java)
        startActivity(intent)
    }

}