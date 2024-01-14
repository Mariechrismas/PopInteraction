package com.example.popinteraction.parameter

import ImagesUpload
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.popinteraction.R
import com.example.popinteraction.XMLWriter

class UploadPopup : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var emoji1: EditText
    private lateinit var emoji2: EditText
    private lateinit var emoji3: EditText
    private lateinit var category: Spinner
    private lateinit var clue: EditText
    private lateinit var answer: EditText
    private lateinit var uploadImageView: ImageView
    private lateinit var imageDownloadImageView: ImageView

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    private val initialListCategory = listOf("Film", "Series", "Game", "Animal", "Actor", "Anime", "Cartoon", "Singer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_upload)

        name = findViewById(R.id.nameEditText)
        emoji1 = findViewById(R.id.emoji1EditText)
        emoji2 = findViewById(R.id.emoji2EditText)
        emoji3 = findViewById(R.id.emoji3EditText)
        clue = findViewById(R.id.clueEditText)
        answer = findViewById(R.id.answerEditText)
        category = findViewById(R.id.spinnerCategory)
        uploadImageView = findViewById(R.id.upload)
        imageDownloadImageView = findViewById(R.id.imageDownload)

        category = findViewById(R.id.spinnerCategory)
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, initialListCategory)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category.adapter = categoryAdapter

        uploadImageView.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handle the result from the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data!!

            imageDownloadImageView.setImageURI(selectedImageUri)
        }
    }

    fun navigateToUpload(view: View) {
        val intent = Intent(this, UploadActivity::class.java)
        startActivity(intent)
    }

    fun uploadData(view: View) {
        val xmlWriter = XMLWriter(this)

        if (validateFields()) {
            if (selectedImageUri != null) {
                val imageFileName = name.text.toString().replace(" ", "_")
                val imagesUpload = ImagesUpload(this, contentResolver)
                val imagePath = imagesUpload.saveImageToInternalStorage(selectedImageUri!!, "$imageFileName.png")

                xmlWriter.addUpload(
                    name = name.text.toString().trim(),
                    category = category.selectedItem.toString(),
                    emoji1 = emoji1.text.toString().trim(),
                    emoji2 = emoji2.text.toString().trim(),
                    emoji3 = emoji3.text.toString().trim(),
                    clue = clue.text.toString().trim(),
                    image = imagePath,
                    listAnswerString = answer.text.toString().trim()
                )

                val intent = Intent(this, UploadActivity::class.java)
                startActivity(intent)
            }
            else{
                    Toast.makeText(this, "Image manquante", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validateField(view: View, delayMillis: Long): Boolean {
        when (view) {
            is EditText -> {
                if (view.text.toString().trim().isEmpty()) {
                    view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                    Handler(Looper.getMainLooper()).postDelayed({
                        view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    }, delayMillis)
                    return false
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    return true
                }
            }
            is Spinner -> {
                if (view.selectedItem == null || view.selectedItem.toString().trim().isEmpty()) {
                    view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                    Handler(Looper.getMainLooper()).postDelayed({
                        view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    }, delayMillis)
                    return false
                } else {
                    view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    return true
                }
            }
        }
        return true
    }

    private fun isValidEmoji(input: String): Boolean {
        val emojiRegex = Regex("([\\x{1F600}-\\x{1F64F}]|[\\x{1F300}-\\x{1F5FF}]|[\\x{1F680}-\\x{1F6FF}]|[\\x{1F700}-\\x{1F77F}]|[\\x{1F780}-\\x{1F7FF}]|[\\x{1F800}-\\x{1F8FF}]|[\\x{1F900}-\\x{1F9FF}]|[\\x{2600}-\\x{26FF}]|[\\x{2700}-\\x{27BF}]|[\\x{2300}-\\x{23FF}])")

        return emojiRegex.containsMatchIn(input)
    }

    private fun validateFields(): Boolean {
        val delayMillis = 250L

        val isValidName = validateField(name, delayMillis)
        val isValidEmoji1 = validateEmojiField(emoji1, delayMillis)
        val isValidEmoji2 = validateEmojiField(emoji2, delayMillis)
        val isValidEmoji3 = validateEmojiField(emoji3, delayMillis)
        val isValidCategory = validateField(category, delayMillis)
        val isValidClue = validateField(clue, delayMillis)
        val isValidAnswer = validateField(answer, delayMillis)

        val isAllValid = isValidName && isValidEmoji1 && isValidEmoji2 && isValidEmoji3 && isValidCategory && isValidClue && isValidAnswer


        if (!isAllValid) {
            Toast.makeText(this, "Tous les champs n'ont pas été remplis correctement", Toast.LENGTH_LONG).show()
        }

        return isAllValid
    }

    private fun validateEmojiField(view: EditText, delayMillis: Long): Boolean {
        val input = view.text.toString().trim()

        return if (input.isEmpty() || !isValidEmoji(input)) {
            view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            Handler(Looper.getMainLooper()).postDelayed({
                view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
            }, delayMillis)
            false
        } else {
            view.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
            true
        }
    }

}
