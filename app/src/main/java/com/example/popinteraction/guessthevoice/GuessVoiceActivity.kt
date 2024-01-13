package com.example.popinteraction.guessthevoice

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.R
import android.media.MediaPlayer
import android.view.View
import android.widget.Chronometer
import com.example.popinteraction.emojistory.DataObject
import com.example.popinteraction.emojistory.EmojiStoryCurrentObject
import com.example.popinteraction.emojistory.EmojiStoryScore
import kotlin.random.Random

class GuessVoiceActivity : AppCompatActivity() {
    private lateinit var answer: EditText
    private lateinit var displayScore: TextView
    private lateinit var theme: TextView
    private lateinit var validateButton: ToggleButton
    private lateinit var imageSolution: ImageView

    private var listOfDataObject: MutableList<DataObject> = mutableListOf()
    private var score = 0
    private var currentParty: Int = 0
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_voice)
        initializeViews()
        InitGame(2)
    }

    //On initialise les views
    private fun initializeViews() {
        validateButton = findViewById(R.id.validateButton)
        imageSolution = findViewById(R.id.imagesolution)
        displayScore = findViewById(R.id.score)
        theme = findViewById(R.id.theme)
        answer = findViewById(R.id.selectionEditText)
    }

    //Methode pour initialiser le jeu. "numberOfLevel" correspond au nombre d'objet à deviner
    private fun InitGame(numberOfLevel: Int){
        val xmlReadFile = com.example.popinteraction.XMLReadFile()
        val tmpListDataObjects = xmlReadFile.readXmlDataObjects(this)

        while(listOfDataObject.size != numberOfLevel && tmpListDataObjects.isNotEmpty()){
            val randomLevel = Random.nextInt(tmpListDataObjects.size)
            if(tmpListDataObjects[randomLevel].music.isNotEmpty()){
                listOfDataObject.add(tmpListDataObjects[randomLevel])
            }
            tmpListDataObjects.remove(tmpListDataObjects[randomLevel])
        }

        initNewParty()
    }

    //Methode pour initialiser une partie
    fun initNewParty(){
        theme.text = resources.getString(R.string.theme) + ": " + listOfDataObject[currentParty]
        displayScore.text = resources.getString(R.string.score) + ": " + score
        imageSolution.setImageResource(R.drawable.transparent_picture)
        val audioPath = listOfDataObject[currentParty].music
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioPath)
            prepare()
        }

        mediaPlayer?.start()
    }

    //Methode appelé quand l'utilisateur essaye une reponse
    fun validateWord(view: View) {
        val userInput = answer.text.toString()
        if(listOfDataObject[currentParty].listAnswerString.contains(userInput)){

        }
    }
}