package com.example.popinteraction

import android.app.AlertDialog
import android.content.Context
import android.media.MediaPlayer
import java.io.File
import java.io.IOException

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun start(filePath: String) {
        try {
            stop()

            if (filePath.contains("raw")) {
                val rawPart = filePath.substringAfter("/raw/", filePath).substringBeforeLast(".")
                val resId = context.resources.getIdentifier(rawPart, "raw", context.packageName)

                if (resId != 0) {
                    mediaPlayer = MediaPlayer.create(context, resId)
                    mediaPlayer?.start()
                } else {
                    showFileNotFoundDialog()
                }
            } else {
               //Recupération de fichiers sonore dans le telephone
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun showFileNotFoundDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.resources.getString(R.string.no_sound_file))
            .setMessage(context.resources.getString(R.string.no_sound_file_message))
            .setPositiveButton(context.resources.getString(R.string.close), null)
            .show()
    }

    private fun permissionDeniedDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.resources.getString(R.string.permission_denied))
            .setMessage(context.resources.getString(R.string.permission_denied_message))
            .setPositiveButton(context.resources.getString(R.string.close), null)
            .show()
    }
}
