package com.jans.societyoo.ui.societyservice

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jans.societyoo.R
import com.jans.societyoo.viewmodel.SocietyServicesViewModel
import com.jans.societyoo.viewmodel.SocityServicesViewModelFactory
import kotlinx.android.synthetic.main.activity_society_micro_services.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class SocietyMicroServicesActivity : AppCompatActivity() {
    lateinit var viewModel: SocietyServicesViewModel
    private var serviceID: Int = 0
    private var serviceHeaderTitle: String = ""

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_society_micro_services)
        if (intent.extras != null) {
            serviceID = intent.extras!!.getInt("service_id")
            serviceHeaderTitle = intent.extras!!.getString("service_headerTitle")!!
        }

        supportActionBar!!.setTitle(serviceHeaderTitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProvider(viewModelStore, SocityServicesViewModelFactory(this)).get(
            SocietyServicesViewModel::class.java
        )

        list_MicroService.layoutManager =LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        getDashboardServices()
    }

    private fun getDashboardServices() {
        if(serviceID!=null && serviceID!=0){
            viewModel.microServiceLiveData.observe(this, Observer {
                val result = it
                if (result != null && result.size > 0) {
                    list_MicroService.adapter = MicroServiceAdapter(context = this, dataSource = result)
                    list_MicroService.adapter!!.notifyDataSetChanged()
                }
            })
            viewModel.getMicroServicesDB(serviceID)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
