package com.example.popinteraction.emojistory

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.MainActivity
import com.example.popinteraction.R

class EmojiStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emojie_story)

        val xmlReadFile = XMLReadFile()
        val emojiObjectsList = xmlReadFile.readXmlEmojiObjects(this)

        for (emojiObject in emojiObjectsList) {
            Log.d("EmojiStoryActivity", "Nom : ${emojiObject.name}, Catégorie : ${emojiObject.categorie}, Image1 : ${emojiObject.image1}, Image2 : ${emojiObject.image2}, Image3 : ${emojiObject.image3}, ResponseImage : ${emojiObject.responseImage}")
        }
    }



    fun navigateToMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}