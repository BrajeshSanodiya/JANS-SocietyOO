package com.jans.societyoo.ui.societyservice

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jans.imageload.DefaultImageLoader
import com.jans.societyoo.R
import com.jans.societyoo.model.main.Service
import de.hdodenhof.circleimageview.CircleImageView


/*class ServiceAdapter(private val context: Context,
                      private val dataSource: ArrayList<Service>):RecyclerView.Adapter<ViewHolder>() {
    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.title?.text = dataSource.get(position).name
        //holder?.title?.text = dataSource.get(position).name
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val title = view.title_serviceItem
    val logo = view.logo_serviceItem
}*/


class ServiceAdapter(
    dataSource: List<Service>,
    context: Context
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {
    private val serviceList: List<Service>
    var context: Context
    init {
        this.serviceList = dataSource
        this.context = context
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ServiceViewHolder {
        val groceryProductView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_service, parent, false)
        return ServiceViewHolder(groceryProductView)
    }

    override fun onBindViewHolder(
        holder: ServiceViewHolder,
        position: Int
    ) {
        //holder.imageView.setImageResource(serviceList[position].getProductImage())
        holder.title.setText(serviceList[position].name)
        holder.logo.setOnClickListener(View.OnClickListener {
            val productName: String =
                serviceList[position].name.toString()
            //Toast.makeText(context, "$productName is selected", Toast.LENGTH_SHORT).show()
            var intent= Intent(context, SocietyMicroServicesActivity::class.java)
            intent.putExtra("service_id",serviceList[position].id)
            intent.putExtra("service_headerTitle",serviceList[position].headerTitle)
            context.startActivity(intent)
        })

        DefaultImageLoader.load(holder.logo, serviceList[position].img,null)

    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    inner class ServiceViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        var logo: CircleImageView
        var title: TextView
        init {
            logo = view.findViewById(R.id.logo_serviceItem)
            title = view.findViewById(R.id.title_serviceItem)
        }
    }
}
