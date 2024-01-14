package com.example.popinteraction.emojistory

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.R
import com.example.popinteraction.ShowImage
import com.example.popinteraction.XMLReadFile
import kotlin.random.Random

class EmojiStoryActivity : AppCompatActivity() {
    private val emojiStoryPartyObject : EmojiStoryPartyObject = EmojiStoryPartyObject()
    private lateinit var displayEmoji1: TextView
    private lateinit var displayEmoji2: TextView
    private lateinit var displayEmoji3: TextView
    private lateinit var answer: EditText
    private lateinit var score: TextView
    private lateinit var theme: TextView
    private lateinit var indice: TextView
    private lateinit var validateButton: ToggleButton
    private lateinit var imageSolution: ImageView
    private var currentParty: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emoji_story)
        InitGame(10);
    }

    //Methode pour initialiser la party. "numberOfLevel" correspond au nombre d'EmojiStoryObject à deviner
    private fun InitGame(numberOfLevel: Int){
        val xmlReadFile = XMLReadFile
        val emojiObjectsList = xmlReadFile.readXmlEmojieObjects(this)

        for(i in 0 until numberOfLevel){
            if (emojiObjectsList.isNotEmpty()){
                val randomLevel = Random.nextInt(emojiObjectsList.size)
                emojiStoryPartyObject.levelList.add(emojiObjectsList[randomLevel])
                emojiObjectsList.removeAt(randomLevel)
            }
        }

        emojiStoryPartyObject.currentParty = EmojiStoryCurrentObject(emojiStoryPartyObject.levelList.first())
        validateButton = findViewById(R.id.validateButton)
        imageSolution = findViewById(R.id.imagesolution)
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

        if (currentParty < emojiStoryPartyObject.levelList.size - 1) {
            currentParty++
            emojiStoryPartyObject.currentParty = EmojiStoryCurrentObject(emojiStoryPartyObject.levelList[currentParty])
        } else {
            val intent = Intent(this, EmojiStoryScore::class.java)
            intent.putExtra("Score", emojiStoryPartyObject.score.toString())
            startActivity(intent)
        }

        theme.text = resources.getString(R.string.theme) + ": " + emojiStoryPartyObject.currentParty.category
        score.text = resources.getString(R.string.score) + ": " + emojiStoryPartyObject.score
        displayEmoji1.text = emojiStoryPartyObject.currentParty.emoji1
        displayEmoji2.text = "❓"
        displayEmoji3.text = "❓"
        imageSolution.setImageResource(R.drawable.transparent_picture)

    }

    //On calcul les scores en fonction du nombre de tentative
    fun calculateScore(){
        when(emojiStoryPartyObject.currentParty.numberOfEmojiDisplay){
            1 -> emojiStoryPartyObject.score += 4
            2 -> emojiStoryPartyObject.score += 3
            3 -> emojiStoryPartyObject.score += 2
            4 -> emojiStoryPartyObject.score += 1
        }
    }

    //On regarde si la valeur proposé est bonne et on réagit en conséquence
     fun validateWord(view: View){
        val userInput = answer.text.toString()

        if(emojiStoryPartyObject.currentParty.answerIsGood){
            initNewParty()
        }else {
            if (emojiStoryPartyObject.currentParty.listAnswerString.contains(userInput.toUpperCase()) || emojiStoryPartyObject.currentParty.numberOfEmojiDisplay >= 4) {
                emojiStoryPartyObject.currentParty.numberOfEmojiDisplay++
                calculateScore()
                answer.setText(resources.getString(R.string.congratulation))
                validateButton.text = resources.getString(R.string.next_party)
                val resourceId = ShowImage.readAppImage(this, emojiStoryPartyObject.currentParty.image)
                if (resourceId > 0) {
                    imageSolution.setImageResource(resourceId)
                } else {
                    val localImageFile =
                        ShowImage.readLocalImage(this, emojiStoryPartyObject.currentParty.image)
                    if (localImageFile.exists()) {
                        val localBitmap = BitmapFactory.decodeFile(emojiStoryPartyObject.currentParty.image)
                        imageSolution.setImageBitmap(localBitmap)
                    } else {
                        Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
                    }
                }


                emojiStoryPartyObject.currentParty.answerIsGood = true
            } else if (emojiStoryPartyObject.currentParty.numberOfEmojiDisplay < 4) {
                emojiStoryPartyObject.currentParty.numberOfEmojiDisplay++
                when (emojiStoryPartyObject.currentParty.numberOfEmojiDisplay) {
                    1 -> displayEmoji1.text = emojiStoryPartyObject.currentParty.emoji1
                    2 -> displayEmoji2.text = emojiStoryPartyObject.currentParty.emoji2
                    3 -> displayEmoji3.text = emojiStoryPartyObject.currentParty.emoji3
                    4 -> indice.text = resources.getString(R.string.indice) + ": " + emojiStoryPartyObject.currentParty.clue
                }
            }

            score.text = resources.getString(R.string.score) + ": " + emojiStoryPartyObject.score
        }

        answer.setText("")
    }

    //Methode pour retourner à la page précédente
    fun navigateToMenu(view: View) {
        val intent = Intent(this, EmojiStoryFrontPage::class.java)
        startActivity(intent)
    }
}