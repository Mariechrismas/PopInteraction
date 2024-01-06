package com.example.popinteraction.depixelimage

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
object Pixelisation {

    fun pixelateBitmap(resources: Resources, resourceId: Int, pixelationLevel: Int): Bitmap {
        val originalBitmap = getBitmapFromDrawable(resources, resourceId)
        return pixelateBitmap(originalBitmap, pixelationLevel)
    }

    fun pixelateBitmap(originalBitmap: Bitmap, pixelationLevel: Int): Bitmap {
        val pixelatedBitmap = Bitmap.createBitmap(originalBitmap.width, originalBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(pixelatedBitmap)
        val paint = Paint()

        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, originalBitmap.width / pixelationLevel, originalBitmap.height / pixelationLevel, false)
        canvas.drawBitmap(scaledBitmap, null, canvas.clipBounds, paint)

        return pixelatedBitmap
    }

    private fun getBitmapFromDrawable(resources: Resources, resourceId: Int): Bitmap {
        val drawable = resources.getDrawable(resourceId, null)
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val width = if (drawable.intrinsicWidth <= 0) 1 else drawable.intrinsicWidth
        val height = if (drawable.intrinsicHeight <= 0) 1 else drawable.intrinsicHeight

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}
