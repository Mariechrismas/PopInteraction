package com.example.popinteraction.emojistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.MainActivity
import com.example.popinteraction.R

class EmojiStoryFrontPage: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emojie_story_frontpage)

    }

    fun play(view: View){
        val intent = Intent(this, EmojiStoryActivity::class.java)
        startActivity(intent)
    }

    //Methode pour retourner à la page précédente
    fun navigateToMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun showRulesPopup(view: View) {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.CustomDialog)
        alertDialogBuilder.setTitle(R.string.rules_popup_title)
        alertDialogBuilder.setMessage(R.string.rules_emoji_story)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}