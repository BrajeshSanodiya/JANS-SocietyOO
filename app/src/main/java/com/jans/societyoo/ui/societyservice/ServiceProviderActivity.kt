package com.jans.societyoo.ui.societyservice

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jans.societyoo.R
import com.jans.societyoo.model.main.MicroService
import com.jans.societyoo.model.main.Provider
import com.jans.societyoo.viewmodel.SocietyServicesViewModel
import com.jans.societyoo.viewmodel.SocityServicesViewModelFactory
import kotlinx.android.synthetic.main.activity_services_provider.*

class ServiceProviderActivity : AppCompatActivity() {
    lateinit var viewModel: SocietyServicesViewModel
    private var microServiceID: Int = 0
    private var microServiceHeaderTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services_provider)
        if (intent.extras != null) {
            microServiceID = intent.extras!!.getInt("microservice_id")
            microServiceHeaderTitle = intent.extras!!.getString("microservice_headerTitle")!!
        }

        supportActionBar!!.setTitle(microServiceHeaderTitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProvider(viewModelStore, SocityServicesViewModelFactory(this)).get(
            SocietyServicesViewModel::class.java
        )

        list_serviceProvider.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        getDashboardServices()
    }

    private fun getDashboardServices() {
        if (microServiceID != null && microServiceID != 0) {
            viewModel.serviceProviderLiveData.observe(this, Observer {
                val result = it
                if (result != null && result.size > 0) {
                    list_serviceProvider.adapter =
                        ServiceProviderAdapter(context = this, dataSource = getDummyList(result))
                    list_serviceProvider.adapter!!.notifyDataSetChanged()
                }
            })
            viewModel.getServiceProviderDB(microServiceID)
        }
    }

    fun getDummyList(list: List<Provider>): List<Provider> {
//        if (list != null && list.size > 0) {
//            val tempList: ArrayList<Provider> = ArrayList<Provider>()
//            for (item in list) {
//                tempList.add(item)
//            }
//            val tempItem = list.get(0)
//            for (item in 0..9) {
//                tempList.add(tempItem)
//            }
//            return tempList
//        }
        return list
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
