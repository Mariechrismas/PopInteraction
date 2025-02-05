package com.example.popinteraction

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.depixelimage.DepixelimageMenu
import com.example.popinteraction.emojistory.EmojiStoryFrontPage
import com.example.popinteraction.guessvoice.GuessVoiceMenu
import com.example.popinteraction.parameter.ParameterActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun navigateToParameter(view: View) {
        val intent = Intent(this, ParameterActivity::class.java)
        startActivity(intent)
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
        val intent = Intent(this, GuessVoiceMenu::class.java)
        startActivity(intent)
    }
}
