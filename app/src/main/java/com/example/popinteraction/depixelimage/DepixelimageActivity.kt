package com.example.popinteraction.depixelimage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.MainActivity
import com.example.popinteraction.R
import com.example.popinteraction.XMLReadFile
import com.example.popinteraction.emojistory.DataObject

class DepixelimageActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var imageName: String
    private lateinit var clueTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var stopToggleButton: ToggleButton
    private lateinit var submitToggleButton: ToggleButton
    private lateinit var nextToggleButton: ToggleButton
    private var score = 0
    private var isTimerRunning = true
    private var pixelLevel = 100
    private var imageIndex = 0
    private var imagesShown = 0
    private val handler = Handler()
    private var startTime: Long = 0
    private var stopTime: Long = 0
    private lateinit var dataObjects: List<DataObject>
    private val shownImageIndices = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depixelimage)

        initializeViews()

        val xmlReadFile = XMLReadFile()
        dataObjects = xmlReadFile.readXmlDataObjects(this)

        showNextImage()

        setButtonListeners()
    }

    private fun initializeViews() {
        imageView = findViewById(R.id.image)
        clueTextView = findViewById(R.id.clue)
        answerEditText = findViewById(R.id.Answer)
        stopToggleButton = findViewById(R.id.stop)
        submitToggleButton = findViewById(R.id.submit)
        nextToggleButton = findViewById(R.id.next)
    }

    private fun setButtonListeners() {
        stopToggleButton.setOnClickListener {
            onToggleStopButton()
        }

        submitToggleButton.setOnClickListener {
            onSubmitButtonClicked()
        }

        nextToggleButton.setOnClickListener {
            onNextToggleButtonClicked()
        }
    }

    private fun onToggleStopButton() {
        if (isTimerRunning) {
            handler.removeCallbacks(depixelizeImageRunnable)
            stopTime = System.currentTimeMillis()
        } else {
            handler.postDelayed(depixelizeImageRunnable, 2000 - (stopTime - startTime))
        }
        isTimerRunning = !isTimerRunning
    }

    private fun onSubmitButtonClicked() {
        val userInput = answerEditText.text.toString().toLowerCase()
        val answer = dataObjects[imageIndex - 1].listAnswerString[0].trim().toLowerCase()

        if (userInput == answer) {
            handleCorrectAnswer()
        } else {
            answerEditText.setBackgroundColor(resources.getColor(android.R.color.holo_red_dark, null))
        }
    }

    private fun handleCorrectAnswer() {
        val timeTakenInSeconds = if (isTimerRunning) {
            (System.currentTimeMillis() - startTime) / 1000
        } else {
            (stopTime - startTime) / 1000
        }

        val points = calculatePoints(timeTakenInSeconds)

        score += points

        showToast("You earned $points points! Score: $score")

        answerEditText.text = null

        clueTextView.visibility = View.INVISIBLE
        showNextImage()
    }

    private fun calculatePoints(timeTakenInSeconds: Long): Int {
        return when {
            timeTakenInSeconds < 5 -> 5
            timeTakenInSeconds < 10 -> 4
            timeTakenInSeconds < 15 -> 3
            timeTakenInSeconds < 20 -> 2
            else -> 1
        }
    }

    private fun onNextToggleButtonClicked() {
        clueTextView.visibility = View.INVISIBLE
        startTime = 0
        showNextImage()
    }

    private fun showNextImage() {
        if (imagesShown == 10) {
            showFinalScore()
            return
        }

        pixelLevel = 100

        handler.removeCallbacksAndMessages(null)

        // Get a list of indices that haven't been shown yet
        val availableIndices = getAvailableIndices()

        // If all images have been shown, reset the set
        if (availableIndices.isEmpty()) {
            shownImageIndices.clear()
        }

        // Randomly select an index from the available ones
        val randomIndex = availableIndices.shuffled().firstOrNull()

        if (randomIndex != null) {
            val currentDataObject = dataObjects[randomIndex]
            shownImageIndices.add(randomIndex)

            imageName = currentDataObject.image
            val clueText = "This is a ${currentDataObject.categorie}"

            val originalBitmap =
                resources.getIdentifier(imageName, "drawable", packageName)
            val pixelatedBitmap = Pixelisation.pixelateBitmap(resources, originalBitmap, pixelLevel)

            imageView.setImageBitmap(pixelatedBitmap)

            handler.postDelayed({
                clueTextView.text = clueText
                clueTextView.visibility = View.VISIBLE
                startTime = System.currentTimeMillis()
            }, 10000)

            isTimerRunning = true

            stopToggleButton.isChecked = false

            handler.postDelayed(depixelizeImageRunnable, 2000)

            imagesShown++
        }
    }

    private fun getAvailableIndices(): List<Int> {
        return (0 until dataObjects.size).filter { it !in shownImageIndices }
    }

    private val depixelizeImageRunnable = object : Runnable {
        override fun run() {
            if (pixelLevel > 10) {
                pixelLevel -= 10
            } else {
                pixelLevel = 1
            }

            val originalBitmap =
                resources.getIdentifier(imageName, "drawable", packageName)
            val pixelatedBitmap =
                Pixelisation.pixelateBitmap(resources, originalBitmap, pixelLevel)
            imageView.setImageBitmap(pixelatedBitmap)

            handler.postDelayed(this, 2000)
        }
    }

    private fun showFinalScore() {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.CustomDialog)
        alertDialogBuilder.setTitle("Score:")
        alertDialogBuilder.setMessage("Your final score is $score")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
