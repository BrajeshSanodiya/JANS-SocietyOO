package com.jans.societyoo.ui.customviews

import androidx.recyclerview.widget.RecyclerView

abstract class MyRecyclerScroll : RecyclerView.OnScrollListener(){
    private val HIDE_THRESHOLD = 200f
    private val SHOW_THRESHOLD = 100f

    var scrollDist = 0
    var isVisible = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        //  Check scrolled distance against the minimum
        if (isVisible && scrollDist > HIDE_THRESHOLD) {
            //  Hide fab & reset scrollDist
            hide();
            scrollDist = 0;
            isVisible = false;

        }
        //  -MINIMUM because scrolling up gives - dy values
        else if (!isVisible && scrollDist < -SHOW_THRESHOLD) {
            //  Show fab & reset scrollDist
            show();

            scrollDist = 0;
            isVisible = true;
        }

        //  Whether we scroll up or down, calculate scroll distance
        if ((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }
    }

    abstract fun show()
    abstract fun hide()

}