package com.example.pum_lista3.imagePreview

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.example.pum_lista3.R
import com.example.pum_lista3.databinding.FragmentImagePreviewBinding

class ImagePreview : Fragment() {
    private lateinit var binding: FragmentImagePreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePreviewBinding.inflate(inflater, container, false)

        setToolbarParams()
        getImageBitmapFromArgs()?.let {
            binding.imageView.setImageBitmap(it)
        }

        return binding.root
    }

    private fun setToolbarParams() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).title = "Podgląd zdjęcia"
    }

    private fun getImageBitmapFromArgs(): Bitmap? {
        val bundle: Bundle = arguments ?: return null
        val args = ImagePreviewArgs.fromBundle(bundle)
        return args.imageBitmap
    }
}