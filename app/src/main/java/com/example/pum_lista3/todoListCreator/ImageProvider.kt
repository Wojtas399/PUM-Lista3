package com.example.pum_lista3.todoListCreator

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.flow.MutableStateFlow

class ImageProvider(
    registry: ActivityResultRegistry
) {
    private var imageResultLauncher: ActivityResultLauncher<Intent> =
        registry.register("key", ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                selectedImageUri.value = data?.data
            }
        }

    val selectedImageUri: MutableStateFlow<Uri?> = MutableStateFlow(null)

    fun selectImage() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        imageResultLauncher.launch(intent)
    }
}