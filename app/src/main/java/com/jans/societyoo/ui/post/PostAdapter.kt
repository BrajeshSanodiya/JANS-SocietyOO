package com.jans.societyoo.ui.post

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.custom.sliderimage.logic.SliderImage
import com.jans.societyoo.BuildConfig
import com.jans.societyoo.databinding.ListItemPostBinding
import com.jans.societyoo.model.post.Post
import com.jans.societyoo.utils.Constants
import com.jans.societyoo.utils.PrintMsg


class PostAdapter(dataSource: List<Post>, context: Context) :
    RecyclerView.Adapter<PostAdapter.ServiceProviderViewHolder>() {
    private var dataSource: List<Post>
    var context: Context

    init {
        this.dataSource = dataSource
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceProviderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemPostBinding.inflate(layoutInflater, parent, false)
        return ServiceProviderViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ServiceProviderViewHolder,
        position: Int
    ) {
        val data: Post = dataSource[position]
        holder.bind(data)

        if (data.images.size > 1) {
            holder.binding.sliderPostItem.visibility = View.VISIBLE
            holder.binding.singleImagePostItem.visibility = View.GONE
        } else if (data.images.size == 1) {
            holder.binding.sliderPostItem.visibility = View.GONE
            holder.binding.singleImagePostItem.visibility = View.VISIBLE
            holder.binding.singleImagePostItem.setOnClickListener {
                SliderImage.openfullScreen(context, data.images, 0)
            }
        }

        holder.binding.btnSharePostItem.setOnClickListener {
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

    override fun getItemCount(): Int {
        return dataSource.size
    }

    public fun setData(dataSource: List<Post>){
        this.dataSource=dataSource
        this.notifyDataSetChanged()
    }

    inner class ServiceProviderViewHolder(val binding: ListItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Post) {
            binding.post = data
            binding.executePendingBindings()
        }
    }

}