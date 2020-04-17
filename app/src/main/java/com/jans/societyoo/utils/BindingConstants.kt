package com.jans.societyoo.utils

import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

@BindingAdapter("android:loadHtmlText")
fun loadHtmlText(textView: TextView, text: String?){
    if(text!=null && text.isNotEmpty()){
        textView.visibility=View.VISIBLE
        val tempString=text.trim()
        val finalString=tempString.replace("\n","<br />", false)
       // text.replace("\n","<br />", false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            textView.text = Html.fromHtml(finalString, Html.FROM_HTML_MODE_COMPACT)
        } else {
            textView.text = Html.fromHtml(finalString)
        }

    }else{
        textView.visibility=View.GONE
    }
}
