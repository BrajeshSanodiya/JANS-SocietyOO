package com.jans.societyoo.ui.services

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.model.services.MicroService
import com.jans.societyoo.viewmodel.SocietyServicesViewModel
import com.jans.societyoo.viewmodel.SocityServicesViewModelFactory
import kotlinx.android.synthetic.main.activity_micro_services.*

class MicroServicesActivity : AppCompatActivity() {
    lateinit var viewModel: SocietyServicesViewModel
    private var serviceID: Int = 0
    private var serviceHeaderTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_micro_services)
        if (intent.extras != null) {
            serviceID = intent.extras!!.getInt("service_id")
            serviceHeaderTitle = intent.extras!!.getString("service_headerTitle")!!
        }

        supportActionBar!!.setTitle(serviceHeaderTitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProvider(viewModelStore, SocityServicesViewModelFactory(this)).get(
            SocietyServicesViewModel::class.java
        )

        //list_MicroService.layoutManager =LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        getDashboardServices()
    }

    private fun getDashboardServices() {
        if(serviceID!=null && serviceID!=0){
            viewModel.microServiceLiveData.observe(this, Observer {
                val result = it
                if (result != null && result.size > 0) {
                    list_MicroService.adapter = MicroServiceAdapter(context = this, dataSource = getDummyList(result))
                    //list_MicroService.adapter!!.notifyDataSetChanged()
                }
            })
            viewModel.getMicroServicesDB(serviceID)
        }
    }

    fun getDummyList(list: List<MicroService>): List<MicroService> {
        /*if (list != null && list.size > 0) {
            val tempList: ArrayList<MicroService> = ArrayList<MicroService>()
            for (item in list) {
                tempList.add(item)
            }
            val tempItem = list.get(0)
            for (item in 0..9) {
                tempList.add(tempItem)
            }
            return tempList
        }*/
        return list
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
