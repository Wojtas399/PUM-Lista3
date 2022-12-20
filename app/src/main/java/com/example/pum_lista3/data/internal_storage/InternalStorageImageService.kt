package com.example.pum_lista3.data.internal_storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class InternalStorageImageService(
    private val context: Context
) {
    suspend fun loadImage(filename: String): InternalStorageImage? = withContext(Dispatchers.IO) {
        val files = context.filesDir.listFiles()
        files?.filter { it.canRead() && it.isFile && it.name == "$filename.jpg" }?.map {
            val bytes = it.readBytes()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            InternalStorageImage(it.name, bitmap)
        }?.takeIf { it.isNotEmpty() }?.first()
    }

    fun saveImage(filename: String, bitmap: Bitmap) {
        try {
            context.openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                    throw IOException("Couldn't save bitmap")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deleteImage(filename: String) {
        try {
            context.deleteFile(filename)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}