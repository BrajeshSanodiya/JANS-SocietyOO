package com.jans.societyoo.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.jans.societyoo.R

class ImageWithCrossView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {
    var root: View? = null
        private set
    var imgPhoto: ImageView? = null
        private set
    var btnClose: View? = null
        private set
    private val mContext: Context? = null
    private fun init(context: Context) {
        if (isInEditMode) return
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var customView: View? = null
        if (inflater != null) customView = inflater.inflate(R.layout.layout_imagewithcross, this)
        if (customView == null) return
        this.root = customView.findViewById(R.id.root)
        imgPhoto =
            customView.findViewById<View>(R.id.img_photo) as ImageView
        btnClose = customView.findViewById(R.id.btn_close)
    }

    init {
        init(context)
    }
}