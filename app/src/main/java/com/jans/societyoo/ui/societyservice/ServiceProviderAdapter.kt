package com.jans.societyoo.ui.societyservice

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jans.imageload.DefaultImageLoader
import com.jans.imageload.ImageOptions
import com.jans.societyoo.R
import com.jans.societyoo.model.main.MicroService
import com.jans.societyoo.model.main.Provider
import de.hdodenhof.circleimageview.CircleImageView


class ServiceProviderAdapter(dataSource: List<Provider>, context: Context) : RecyclerView.Adapter<ServiceProviderAdapter.ServiceProviderViewHolder>() {
    private val dataSource: List<Provider>
    var context: Context
    init {
        this.dataSource = dataSource
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceProviderViewHolder {
        val groceryProductView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_micro_service, parent, false)
        return ServiceProviderViewHolder(groceryProductView)
    }

    override fun onBindViewHolder(
        holder: ServiceProviderViewHolder,
        position: Int
    ) {
        holder.title.setText(dataSource[position].headerTitle)
        holder.name.setText(dataSource[position].name)
        holder.itemView.setOnClickListener(View.OnClickListener {
            var intent= Intent(context, ServiceProviderDetailActivity::class.java)
            intent.putExtra("provider_id",dataSource[position].id)
            intent.putExtra("provider_headerTitle",dataSource[position].headerTitle)
            context.startActivity(intent)
        })


        var options=ImageOptions(R.drawable.db_splash_logo)
        DefaultImageLoader.load(holder.logo, dataSource[position].img,options)

    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    inner class ServiceProviderViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var logo: CircleImageView
        var title: TextView
        var name: TextView
        init {
            logo = view.findViewById(R.id.logo_microServiceItem)
            title = view.findViewById(R.id.title_microServiceItem)
            name = view.findViewById(R.id.name_microServiceItem)
        }
    }
}
