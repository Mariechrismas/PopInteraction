package com.example.popinteraction.emojistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.R
import kotlin.random.Random

class EmojiStoryActivity : AppCompatActivity() {
    private val emojiStoryParty : EmojiStoryParty = EmojiStoryParty()
    private lateinit var displayEmoji1: TextView
    private lateinit var displayEmoji2: TextView
    private lateinit var displayEmoji3: TextView
    private lateinit var answer: EditText
    private lateinit var score: TextView
    private lateinit var theme: TextView
    private lateinit var indice: TextView
    private var currentParty: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_story)

        InitGame(2);

    }

    //Methode pour initialiser la party. "numberOfLevel" correspond au nombre d'EmojiStoryObject à deviner
    private fun InitGame(numberOfLevel: Int){
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
        theme = findViewById(R.id.theme)
        answer = findViewById(R.id.selectionEditText)
        displayEmoji1 = findViewById(R.id.emoji1)
        displayEmoji2 = findViewById(R.id.emoji2)
        displayEmoji3 = findViewById(R.id.emoji3)
        indice = findViewById(R.id.indice)
        initNewParty()
    }

    //On initialise une nouvelle party
    fun initNewParty(){
        indice.text = " "

        if (currentParty < emojiStoryParty.levelList.size - 1) {
            currentParty++
            emojiStoryParty.currentParty = EmojiStoryCurrent(emojiStoryParty.levelList[currentParty])
        } else {
            val intent = Intent(this, EmojiStoryScore::class.java)
            intent.putExtra("Score", emojiStoryParty.score.toString())
            startActivity(intent)
        }

        theme.text = resources.getString(R.string.theme) + ": " + emojiStoryParty.currentParty.categorie
        score.text = resources.getString(R.string.score) + ": " + emojiStoryParty.score
        displayEmoji1.text = emojiStoryParty.currentParty.image1
        displayEmoji2.text = "❓"
        displayEmoji3.text = "❓"


    }

    //On calcul les scores en fonction du nombre de tentative
    fun calculateScore(){
        when(emojiStoryParty.currentParty.numberOfEmojiDisplay){
            1 -> emojiStoryParty.score += 4
            2 -> emojiStoryParty.score += 3
            3 -> emojiStoryParty.score += 2
            4 -> emojiStoryParty.score += 1
        }
    }

    //On regarde si la valeur proposé est bonne et on réagit en conséquence
     fun validateWord(view: View){
        val userInput = answer.text.toString()

        if (emojiStoryParty.currentParty.listAnswerString.contains(userInput.toUpperCase())) {
            calculateScore()
            initNewParty()
            emojiStoryParty.currentParty.answerIsGood = true
        } else if(emojiStoryParty.currentParty.numberOfEmojiDisplay < 4) {
            emojiStoryParty.currentParty.numberOfEmojiDisplay++
            when(emojiStoryParty.currentParty.numberOfEmojiDisplay){
                1 -> displayEmoji1.text = emojiStoryParty.currentParty.image1
                2 -> displayEmoji2.text = emojiStoryParty.currentParty.image2
                3 -> displayEmoji3.text = emojiStoryParty.currentParty.image3
                4 -> indice.text = resources.getString(R.string.indice) + ": " + emojiStoryParty.currentParty.indice
            }
        }else{
            initNewParty()
        }
        answer.setText("")
        score.text = resources.getString(R.string.score) + ": " + emojiStoryParty.score
    }
}