package com.example.popinteraction.guessvoice

import android.content.Intent
import android.graphics.BitmapFactory
import com.example.popinteraction.AudioPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.R
import com.example.popinteraction.XMLReadFile
import com.example.popinteraction.ShowImage
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class GuessVoiceActivity : AppCompatActivity() {
    private lateinit var answer: EditText
    private lateinit var displayScore: TextView
    private lateinit var theme: TextView
    private lateinit var validateButton: ToggleButton
    private lateinit var imageSolution: ImageView
    private lateinit var timer: TextView

    private var listOfDataObject: MutableList<GuessVoiceObject> = mutableListOf()
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
        InitGame(10)
    }

    private fun initializeViews() {
        validateButton = findViewById(R.id.validateButton)
        imageSolution = findViewById(R.id.imagesolution)
        displayScore = findViewById(R.id.score)
        theme = findViewById(R.id.theme)
        answer = findViewById(R.id.selectionEditText)
        timer = findViewById(R.id.timer)
    }

    private fun InitGame(numberOfLevel: Int){
        val xmlReadFile = XMLReadFile
        val tmpListDataObjects = xmlReadFile.readXmlGuessVoiceObjects(this)
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
        theme.text = resources.getString(R.string.theme) + ": " + listOfDataObject[currentParty].category
        displayScore.text = resources.getString(R.string.score) + ": " + score
        imageSolution.setImageResource(R.drawable.transparent_picture)
        answer.setText("")
        isPartyEnd = true

        resetTimer()
        audioPlayer.start(listOfDataObject[currentParty].music)
        startTimer()
    }

    private fun calculateScore(millisUntilFinished: Long) {
        val secondsRemaining = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        score += when (secondsRemaining) {
            in 0..5 -> 1
            in 5..10 -> 2
            in 10..15 -> 3
            in 15..20 -> 4
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
            timer.text = resources.getString(R.string.congratulation)
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

    private fun partyIsEnd() {
        isPartyEnd = true
        validateButton.text = resources.getString(R.string.next_party)
        val resourceId = ShowImage.readAppImage(this, listOfDataObject[currentParty].image)
        if (resourceId > 0) {
            imageSolution.setImageResource(resourceId)
        } else {
            val localImageFile =
                ShowImage.readLocalImage(this, listOfDataObject[currentParty].image)
            if (localImageFile.exists()) {
                val localBitmap = BitmapFactory.decodeFile(listOfDataObject[currentParty].image)
                imageSolution.setImageBitmap(localBitmap)
            } else {
                Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
            }
        }
        stopTimer()
        audioPlayer.stop()
    }
}