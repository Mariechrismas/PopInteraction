package com.example.popinteraction.emojistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.MainActivity
import com.example.popinteraction.R
import kotlin.random.Random

class EmojiStoryActivity : AppCompatActivity() {
    private var emojiStoryParty : EmojiStoryParty = EmojiStoryParty()
    private lateinit var displayEmoji1: TextView
    private lateinit var displayEmoji2: TextView
    private lateinit var displayEmoji3: TextView
    private lateinit var answer: EditText
    private lateinit var score: TextView
    private lateinit var theme: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_story)

        InitParty(1);

    }

    //Methode pour initialiser la party. "numberOfLevel" correspond au nombre d'EmojiStoryObject à deviner
    private fun InitParty(numberOfLevel: Int){
        val xmlReadFile = XMLReadFile()
        val emojiObjectsList = xmlReadFile.readXmlEmojiObjects(this)

        for(i in 0 until numberOfLevel){
            if (emojiObjectsList.isNotEmpty()){
                val randomLevel = Random.nextInt(emojiObjectsList.size)
                emojiStoryParty.levelList.add(emojiObjectsList[randomLevel])
                emojiObjectsList.removeAt(randomLevel)
            }
        }

        emojiStoryParty.currentParty = EmojiStoryCurrent(emojiStoryParty.levelList.first())
        score = findViewById(R.id.score)
        score.text = resources.getString(R.string.score) + ": " + emojiStoryParty.score

        theme = findViewById(R.id.theme)
        theme.text = resources.getString(R.string.score) + ": " + emojiStoryParty.currentParty.categorie

        answer = findViewById(R.id.selectionEditText)
        displayEmoji1 = findViewById(R.id.emoji1)
        displayEmoji2 = findViewById(R.id.emoji2)
        displayEmoji3 = findViewById(R.id.emoji3)
        displayEmoji1.text = "❓"
        displayEmoji2.text = "❓"
        displayEmoji3.text = "❓"
    }

    //On regarde si la valeur proposé est bonne et on réagit en conséquence
     fun validateWord(view: View){
        val userInput = answer.text.toString()

        if (emojiStoryParty.currentParty.listAnswerString.contains(userInput.toUpperCase())) {
            emojiStoryParty.currentParty.answerIsGood = true
        } else {
            emojiStoryParty.currentParty.numberOfEmojiDisplay++
            answer.setText(" ")
            when(emojiStoryParty.currentParty.numberOfEmojiDisplay){
                1 -> displayEmoji1.text = emojiStoryParty.currentParty.image1
                2 -> displayEmoji2.text = emojiStoryParty.currentParty.image2
                3 -> displayEmoji3.text = emojiStoryParty.currentParty.image3
            }

            emojiStoryParty.score++
            score.text = resources.getString(R.string.score) + ": " + emojiStoryParty.score
        }

    }
}