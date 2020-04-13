package com.jans.societyoo.ui.post

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.custom.sliderimage.logic.SliderImage
import com.jans.societyoo.BuildConfig
import com.jans.societyoo.R
import com.jans.societyoo.databinding.ListItemPostBinding
import com.jans.societyoo.model.post.Post


class PaginationAdapter(dataSource: ArrayList<Post>, context: Context) : RecyclerView.Adapter<PaginationAdapter.ServiceProviderViewHolder>() {
    private var dataSource: ArrayList<Post> = dataSource
    var context: Context = context
    private val LOADING = 0
    private val ITEM = 1
    private var isLoadingAdded = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceProviderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemPostBinding.inflate(layoutInflater, parent, false)
        return ServiceProviderViewHolder(binding,viewType)
    }

    override fun onBindViewHolder(holder: PaginationAdapter.ServiceProviderViewHolder, position: Int) {
        val data: Post = dataSource[position]
        when (getItemViewType(position)) {
            ITEM -> {

                val myHolder = holder as ServiceProviderViewHolder
                myHolder.bind(data)
                if (data.images.size > 1) {
                    myHolder.binding.sliderPostItem.visibility = View.VISIBLE
                    myHolder.binding.singleImagePostItem.visibility = View.GONE
                } else if (data.images.size == 1) {
                    myHolder.binding.sliderPostItem.visibility = View.GONE
                    myHolder.binding.singleImagePostItem.visibility = View.VISIBLE
                    myHolder.binding.singleImagePostItem.setOnClickListener {
                        SliderImage.openfullScreen(context, data.images, 0)
                    }
                }
                myHolder.binding.btnSharePostItem.setOnClickListener {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "${data.desc} " +
                                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                    )
                    sendIntent.type = "text/plain"
                    context.startActivity(sendIntent)
                }
            }
            LOADING -> {
                val loadingViewHolder = holder as LoadingViewHolder
                loadingViewHolder.progressBar.visibility = View.VISIBLE
            }
        }
    }


    override fun getItemCount(): Int {
        return if (dataSource == null) 0 else dataSource.size
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == dataSource.size - 1 && isLoadingAdded) LOADING else ITEM
    }
    fun addLoadingFooter() {
        isLoadingAdded = true
        add(null)
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position: Int = dataSource.size - 1
        val result = getItem(position)
        if (result != null) {
            dataSource.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun add(post: Post?) {
        post?.let {
            dataSource.add(it)
            notifyItemInserted(dataSource.size - 1)
        }
    }

    fun addAll(postResult: List<Post?>) {
        for (result in postResult) {
            add(result)
        }
    }

    fun getItem(position: Int): Post? {
        return dataSource.get(position)
    }


    inner class ServiceProviderViewHolder(
        val binding: ListItemPostBinding,
        viewType: Int
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Post) {

            binding.post = data


            binding.executePendingBindings()
        }
    }

    inner class LoadingViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById<View>(R.id.progressBar) as ProgressBar

    }


}