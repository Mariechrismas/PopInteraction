package com.example.popinteraction.emojistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.MainActivity
import com.example.popinteraction.R
import kotlin.random.Random

class EmojiStoryActivity : AppCompatActivity() {
    private var emojiStoryParty = EmojiStoryParty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emojie_story)

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

        emojiStoryParty.currentParty = emojiStoryParty.levelList.first().toString();
    }


    fun navigateToMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}