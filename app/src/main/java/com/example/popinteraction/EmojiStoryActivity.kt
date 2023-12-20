package com.example.popinteraction

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class EmojiStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emojie_story)
    }

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