package com.jans.societyoo.utils

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.custom.sliderimage.logic.SliderImage
import com.jans.imageload.DefaultImageLoader
import com.jans.imageload.ImageOptions
import com.jans.societyoo.R

@BindingAdapter("android:sliderImageSetItem")
fun sliderImageSetItems(view: SliderImage, listOfUrls: List<String>?) {
    if(listOfUrls!=null && listOfUrls.isNotEmpty())
        view.setItems(listOfUrls)
}

@BindingAdapter("android:loadImage")
fun loadImage(view: ImageView, url: String?){
    if(url!=null && url.isNotEmpty()){
        val options=ImageOptions(placeholder = R.drawable.db_splash_logo,error = R.drawable.db_splash_logo)
        DefaultImageLoader.load(view, url,options)
    }
}
