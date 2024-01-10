package com.example.popinteraction

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.depixelimage.DepixelimageActivity
import com.example.popinteraction.depixelimage.DepixelimageMenu
import com.example.popinteraction.emojistory.EmojiStoryActivity
import com.example.popinteraction.emojistory.EmojiStoryFrontPage

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun navigateToDepixelImage(view: View) {
        val intent = Intent(this, DepixelimageMenu::class.java)
        startActivity(intent)
    }

    fun navigateToEmojiStory(view: View) {
        val intent = Intent(this, EmojiStoryFrontPage::class.java)
        startActivity(intent)
    }

    fun navigateToGuessVoice(view: View) {
        val intent = Intent(this, GuessVoiceActivity::class.java)
        startActivity(intent)
    }
}
