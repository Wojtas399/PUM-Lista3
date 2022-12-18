package com.example.pum_lista3.todoListCreator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.flow.MutableStateFlow

class ImageProvider(
    context: Context,
    registry: ActivityResultRegistry,
) {
    private var imageResultLauncher: ActivityResultLauncher<Intent> =
        registry.register("key", ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = it.data?.data
                val source = uri?.let { imageUri ->
                    ImageDecoder.createSource(context.contentResolver, imageUri)
                }
                selectedImageBitmap.value = source?.let { imageSource ->
                    ImageDecoder.decodeBitmap(imageSource)
                }
            }
        }


    val selectedImageBitmap: MutableStateFlow<Bitmap?> = MutableStateFlow(null)

    fun selectImage() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        imageResultLauncher.launch(intent)
    }
}