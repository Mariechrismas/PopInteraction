package com.example.popinteraction

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import java.io.File

object ShowImage : AppCompatActivity(){

    fun readLocalImage(context: Context, imageName: String): File {
        return File(File(context.filesDir, "images/${imageName.substringAfterLast("/")}").absolutePath)
    }

    fun readAppImage(context: Context, imageName: String): Int {
        return context.resources.getIdentifier(imageName.substringAfterLast("/"), "drawable", context.packageName)
    }

}