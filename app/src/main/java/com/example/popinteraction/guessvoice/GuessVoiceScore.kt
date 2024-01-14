package com.example.popinteraction.guessvoice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.R

class GuessVoiceScore : AppCompatActivity() {
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_voice_score)
        scoreTextView = findViewById(R.id.score)
        val score = intent.getStringExtra("ScoreGuessVoice")
        scoreTextView.text = resources.getString(R.string.score) + " : " + score
    }

    //La partie est terminée on retourne
    fun gameOver(view: View){
        val intent = Intent(this, GuessVoiceMenu::class.java)
        startActivity(intent)
    }
}