package com.example.popinteraction.depixelimage

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.R
import com.example.popinteraction.XMLReadFile
import java.io.File
import java.util.Locale

class DepixelimageActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var imageName: String? = null
    private lateinit var clueTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var stopToggleButton: ToggleButton
    private lateinit var submitToggleButton: ToggleButton
    private lateinit var nextToggleButton: ToggleButton
    private lateinit var scoreView: TextView
    private var score = 0
    private var isTimerRunning = true
    private var pixelLevel = 100
    private var imageIndex = 0
    private var imagesShown = 0
    private val handler = Handler()
    private var startTime: Long = 0
    private var stopTime: Long = 0
    private lateinit var dataObjects: List<DepixelObject>
    private val shownImageCLue = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depixelimage)

        initializeViews()

        val xmlReadFile = XMLReadFile
        dataObjects = xmlReadFile.readXmlDepixelObject(this)

        showNextImage()

        setButtonListeners()

        scoreView.text = resources.getString(R.string.score) + ": " + score
    }

    fun navigateToDepixelMenu(view: View) {
        val intent = Intent(this, DepixelimageMenu::class.java)
        startActivity(intent)
    }

    private fun initializeViews() {
        imageView = findViewById(R.id.image)
        clueTextView = findViewById(R.id.clue)
        answerEditText = findViewById(R.id.Answer)
        stopToggleButton = findViewById(R.id.stop)
        submitToggleButton = findViewById(R.id.submit)
        nextToggleButton = findViewById(R.id.next)
        scoreView = findViewById(R.id.score)
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
        val userInput = answerEditText.text.toString().lowercase(Locale.ROOT)
        val answer = dataObjects[imageIndex - 1].listAnswerString[0].trim().lowercase(Locale.ROOT)

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

        scoreView.text = resources.getString(R.string.score) + ": " + score

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

        val availableIndices = getAvailableIndices()

        if (availableIndices.isEmpty()) {
            shownImageCLue.clear()
        }

        val randomIndex = availableIndices.shuffled().firstOrNull()

        if (randomIndex != null) {
            imageIndex = randomIndex + 1
            val currentDataObject = dataObjects[randomIndex]
            shownImageCLue.add(randomIndex)

            imageName = currentDataObject.image.substringAfterLast("/")

            val imageResourceId = resources.getIdentifier(imageName, "drawable", packageName)

            if (imageResourceId != 0) {
                val originalBitmap = BitmapFactory.decodeResource(resources, imageResourceId)
                imageView.setImageBitmap(originalBitmap)

                val clueText = "This is a ${currentDataObject.clue}"

                val pixelatedBitmap = Pixelisation.pixelateBitmap(originalBitmap, pixelLevel)
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
            } else {
                val localImagePath = File(filesDir, "images/$imageName").absolutePath

                val localImageFile = File(localImagePath)

                if (localImageFile.exists()) {
                    val localBitmap = BitmapFactory.decodeFile(localImagePath)
                    imageView.setImageBitmap(localBitmap)

                    val clueText = "This is a ${currentDataObject.category}"

                    val pixelatedBitmap = Pixelisation.pixelateBitmap(localBitmap, pixelLevel)
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
                } else {
                    Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getAvailableIndices(): List<Int> {
        return (0 until dataObjects.size).filter { it !in shownImageCLue }
    }

    private val depixelizeImageRunnable = object : Runnable {
        override fun run() {
            if (pixelLevel > 10) {
                pixelLevel -= 10
            } else {
                pixelLevel = 1
            }

            val originalBitmap = if (resources.getIdentifier(imageName, "drawable", packageName) != 0) {
                BitmapFactory.decodeResource(resources, resources.getIdentifier(imageName, "drawable", packageName))
            } else {
                val localImagePath = File(filesDir, "images/$imageName").absolutePath
                BitmapFactory.decodeFile(localImagePath)
            }

            val pixelatedBitmap = Pixelisation.pixelateBitmap(originalBitmap, pixelLevel)
            imageView.setImageBitmap(pixelatedBitmap)

            handler.postDelayed(this, 2000)
        }
    }

    private fun showFinalScore() {
        val intent = Intent(this, DepixelimageScore::class.java)
        intent.putExtra("Score", score.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
