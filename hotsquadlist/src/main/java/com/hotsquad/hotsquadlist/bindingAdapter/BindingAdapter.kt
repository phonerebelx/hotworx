package com.hotsquad.hotsquadlist.bindingAdapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hotsquad.hotsquadlist.R
import com.hotsquad.hotsquadlist.extensions.isValid
import com.hotsquad.hotsquadlist.extensions.visible

object BindingAdapter {

    @BindingAdapter("setImageUsingGlide")
    @JvmStatic
    fun setImageUsingGlide(imageView: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty())
            Glide.with(imageView.context).load(imageUrl).placeholder(R.drawable.photo)
                .into(imageView)
    }

    @BindingAdapter("setTextIfNotEmpty")
    @JvmStatic
    fun setTextIfNotEmpty(textView: TextView, text: String) {
        if (text.isValid()) {
            textView.text = text
        } else {
            textView.visible(false)
        }
    }
}