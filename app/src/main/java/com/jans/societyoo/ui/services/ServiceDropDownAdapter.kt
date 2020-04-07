package com.jans.societyoo.ui.services

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.jans.imageload.DefaultImageLoader
import com.jans.societyoo.R
import com.jans.societyoo.model.services.Service

class ServiceDropDownAdapter(val context: Context, var listItemsTxt: List<Service>) : BaseAdapter() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item_service_dropdown, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        // setting adapter item height programatically.

      /*  val params = view.layoutParams
        params.height = 60
        view.layoutParams = params*/
        DefaultImageLoader.load(vh.logo,listItemsTxt.get(position).img)
        vh.name.text = listItemsTxt.get(position).name
        return view
    }

    override fun getItem(position: Int): Any? {
        return listItemsTxt[position]

    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return listItemsTxt.size
    }

    private class ItemRowHolder(row: View?) {
        val logo: ImageView
        val name: TextView

        init {
            this.logo = row?.findViewById(R.id.logo_servcieItem_DropDown) as ImageView
            this.name = row?.findViewById(R.id.title_serviceItem_DropDown) as TextView
        }
    }
}