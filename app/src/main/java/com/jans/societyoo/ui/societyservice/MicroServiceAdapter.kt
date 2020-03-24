package com.jans.societyoo.ui.societyservice

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.jans.imageload.DefaultImageLoader
import com.jans.imageload.ImageOptions
import com.jans.societyoo.R
import com.jans.societyoo.model.main.MicroService
import kotlinx.android.synthetic.main.list_item_micro_service_grid.view.*


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


/*class MicroServiceAdapter( dataSource: List<MicroService>, context: Context) : RecyclerView.Adapter<MicroServiceAdapter.MicroServiceViewHolder>() {
    private val dataSource: List<MicroService>
    var context: Context
    init {
        this.dataSource = dataSource
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MicroServiceViewHolder {
        val groceryProductView: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_micro_service, parent, false)
        return MicroServiceViewHolder(groceryProductView)
    }

    override fun onBindViewHolder(
        holder: MicroServiceViewHolder,
        position: Int
    ) {
        holder.title.setText(dataSource[position].headerTitle)
        holder.name.setText(dataSource[position].name)
        var options = ImageOptions(R.drawable.db_splash_logo)
        DefaultImageLoader.load(holder.logo, dataSource[position].img,options)
        holder.itemView.setOnClickListener{
            var intent= Intent(context, ServiceProviderActivity::class.java)
            intent.putExtra("microservice_id",dataSource[position].id)
            intent.putExtra("microservice_headerTitle",dataSource[position].headerTitle)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    inner class MicroServiceViewHolder(view: View) :
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
}*/


data class MicroServiceAdapter(var dataSource :List<MicroService>, var context: Context) : BaseAdapter(){

    override fun getItem(position: Int): Any {
        return dataSource.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context,R.layout.list_item_micro_service_grid,null)

        view.name_microServiceItem.setText(dataSource[position].name)
        var options = ImageOptions(R.drawable.db_splash_logo)
        DefaultImageLoader.load(view.logo_microServiceItem, dataSource[position].img,options)
        view.logo_microServiceItem.setOnClickListener{
            var intent= Intent(context, ServiceProviderActivity::class.java)
            intent.putExtra("microservice_id",dataSource[position].id)
            intent.putExtra("microservice_headerTitle",dataSource[position].headerTitle)
            context.startActivity(intent)
        }

        return view
    }

}