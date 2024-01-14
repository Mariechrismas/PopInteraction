package com.example.popinteraction.guessthevoice

import android.content.Intent
import com.example.popinteraction.AudioPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.R
import com.example.popinteraction.emojistory.DataObject
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class GuessVoiceActivity : AppCompatActivity() {
    private lateinit var answer: EditText
    private lateinit var displayScore: TextView
    private lateinit var theme: TextView
    private lateinit var validateButton: ToggleButton
    private lateinit var imageSolution: ImageView
    private lateinit var timer: TextView
    private lateinit var solution: TextView

    private var listOfDataObject: MutableList<DataObject> = mutableListOf()
    private var score = 0
    private var currentParty: Int = 0
    private val audioPlayer = AudioPlayer(this)
    private var countDownTimer: CountDownTimer? = null
    private val startTimeInMillis: Long = 20000
    private var currentMillisUntilFinished: Long = 0
    private var isPartyEnd: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guess_voice)
        initializeViews()
        InitGame(2)
    }

    private fun initializeViews() {
        validateButton = findViewById(R.id.validateButton)
        imageSolution = findViewById(R.id.imagesolution)
        displayScore = findViewById(R.id.score)
        theme = findViewById(R.id.theme)
        answer = findViewById(R.id.selectionEditText)
        timer = findViewById(R.id.timer)
        solution = findViewById(R.id.solution)
    }

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

        initTimer()
        initNewParty()
    }

    fun initNewParty(){
        theme.text = resources.getString(R.string.theme) + ": " + listOfDataObject[currentParty].categorie
        displayScore.text = resources.getString(R.string.score) + ": " + score
        imageSolution.setImageResource(R.drawable.transparent_picture)
        answer.setText("")
        solution.text = ""
        isPartyEnd = true

        resetTimer()
        audioPlayer.start(listOfDataObject[currentParty].music)
        startTimer()
        //audioPlayer.start("/storage/self/primary/Music/music_the_lion_king.mp3")
    }

    private fun calculateScore(millisUntilFinished: Long) {
        val secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        score += when (secondsRemaining) {
            in 0..5 -> 1
            in 6..10 -> 2
            in 11..15 -> 3
            in 16..20 -> 4
            else -> 0
        }

        displayScore.text = resources.getString(R.string.score) + ": $score"
    }

    fun validateWord(view: View) {
        val userInput = answer.text.toString()
        if (isPartyEnd){
            currentParty++
            if(currentParty >= listOfDataObject.size) {
                val intent = Intent(this, GuessVoiceScore::class.java)
                intent.putExtra("ScoreGuessVoice", score.toString())
                startActivity(intent)
            }else{
                initNewParty()
            }
        }else if(listOfDataObject[currentParty].listAnswerString.any { it.trim().equals(userInput.trim(), ignoreCase = true)}) {
            calculateScore(currentMillisUntilFinished)
            timer.text = resources.getString(R.string.congratulation    )
            partyIsEnd()
            answer.setText("")
        }
    }

    private fun initTimer() {
        countDownTimer = object : CountDownTimer(startTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                isPartyEnd = false
                timer.text = resources.getString(R.string.timer) + String.format("%02d", seconds)
                currentMillisUntilFinished = millisUntilFinished
            }

            override fun onFinish() {
                timer.text = resources.getString(R.string.time_elapsed)
                partyIsEnd()
            }
        }
    }

    private fun startTimer() {
        countDownTimer?.start()
    }

    private fun resetTimer() {
        countDownTimer?.cancel()
        initTimer()
        timer.text = "20"
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
    }

    fun navigateToMenu(view: View) {
        audioPlayer.stop()
        val intent = Intent(this, GuessVoiceMenu::class.java)
        startActivity(intent)
    }

    private fun partyIsEnd(){
        isPartyEnd = true
        validateButton.text = resources.getString(R.string.next_party)
        val resourceId = resources.getIdentifier(
            listOfDataObject[currentParty].image,
            "drawable",
            packageName
        )
        if (resourceId > 0) {
            imageSolution.setImageResource(resourceId)
        }
        stopTimer()
        audioPlayer.stop()
    }
}