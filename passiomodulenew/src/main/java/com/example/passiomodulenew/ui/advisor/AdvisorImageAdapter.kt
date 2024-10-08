package com.example.passiomodulenew.ui.advisor

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.passio.passiomodulenew.databinding.ItemAdvisorImageBinding

class AdvisorImageAdapter(private var imageUris: List<Bitmap>) :
    RecyclerView.Adapter<AdvisorImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(val binding: ItemAdvisorImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ItemAdvisorImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = imageUris[position]

        holder.binding.foodImage.load(uri)
    }

    override fun getItemCount() = imageUris.size

}