package com.example.popinteraction.guessthevoice

import com.example.popinteraction.AudioPlayer
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.R
import android.view.View
import com.example.popinteraction.emojistory.DataObject
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
    private val audioPlayer = AudioPlayer(this)


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
        theme.text = resources.getString(R.string.theme) + ": " + listOfDataObject[currentParty].categorie
        displayScore.text = resources.getString(R.string.score) + ": " + score
        imageSolution.setImageResource(R.drawable.transparent_picture)

        //audioPlayer.start(listOfDataObject[currentParty].music)
        audioPlayer.start("/storage/self/primary/Music/music_the_lion_king.mp3")
    }

    //Methode appelé quand l'utilisateur essaye une reponse
    fun validateWord(view: View) {
        val userInput = answer.text.toString()
        if(listOfDataObject[currentParty].listAnswerString.contains(userInput)){
            score++
        }
    }
}