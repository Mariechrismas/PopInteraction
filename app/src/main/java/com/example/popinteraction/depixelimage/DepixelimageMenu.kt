package com.example.popinteraction.depixelimage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.popinteraction.MainActivity
import com.example.popinteraction.R

class DepixelimageMenu : AppCompatActivity() {

    private lateinit var playButton: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depixelimage_menu)

        playButton = findViewById(R.id.playButton)

        playButton.isChecked = false
    }

    fun navigateToMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun navigateToPlayDepixelimage(view: View) {
        Log.d("DepixelimageMenu", "Avant de démarrer l'activité")
        val intent = Intent(this, DepixelimageActivity::class.java)
        startActivity(intent)
        Log.d("DepixelimageMenu", "Aprés de démarrer l'activité")
    }

    fun showRulesPopup(view: View) {
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.CustomDialog)
        alertDialogBuilder.setTitle(R.string.rules_popup_title)
        alertDialogBuilder.setMessage(R.string.rules_depixelimage)
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}