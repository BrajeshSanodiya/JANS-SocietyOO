package com.jans.societyoo.ui.services

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.databinding.ActivityServiceProviderDetailBinding
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.services.ProviderDetail
import com.jans.societyoo.utils.Constants
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.SocietyServicesViewModel
import com.jans.societyoo.viewmodel.SocityServicesViewModelFactory
import kotlinx.android.synthetic.main.activity_service_provider_detail.*
import kotlinx.android.synthetic.main.activity_service_provider_detail.view.*


class ServiceProviderDetailActivity : AppCompatActivity() {
    lateinit var viewModel: SocietyServicesViewModel
    private var providerID: Int = 0
    private var providerHeaderTitle: String = ""
    private var binding:ActivityServiceProviderDetailBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_service_provider_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_provider_detail)

        if (intent.extras != null) {
            providerID = intent.extras!!.getInt("provider_id")
            providerHeaderTitle = intent.extras!!.getString("provider_headerTitle")!!
        }

        supportActionBar!!.setTitle(providerHeaderTitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProvider(viewModelStore, SocityServicesViewModelFactory(this)).get(
            SocietyServicesViewModel::class.java
        )
        getProviderDetails()
    }
    private fun getProviderDetails() {
        if (providerID != null && providerID != 0) {
            viewModel.getProviderDetail(providerID).observe(this, Observer {
                val result = it
                if (result is MyResult.Success) {
                    val data: ApiDataObject<ProviderDetail> = result.data
                    if (data.dis_msg == 1 && !TextUtils.isEmpty(data.msg))
                        PrintMsg.toast(this, data.msg);
                    if (data.success_stat == 1) {
                        binding!!.providerDetail=data.data_details
                        //bindUIData(data.data_details)

                    }
                } else if (result is MyResult.Error) {
                    PrintMsg.toastDebug(this, result.message)
                }
            })
        }
    }

    private fun bindUIData(dataDetails: ProviderDetail) {
        title_providerDetail.text=dataDetails.serviceTitle
        name_providerDetail.text=dataDetails.contactInformation.contactPerson
        email_providerDetail.text=dataDetails.contactInformation.email
        mobile_providerDetail.text=dataDetails.contactInformation.phone
        location_providerDetail.text=dataDetails.location
        website_providerDetail.text=dataDetails.contactInformation.website
        whatsapp_providerDetail.text=dataDetails.contactInformation.whatsApp
        workHour_providerDetail.text=dataDetails.contactInformation.workingHours
        about_providerDetail.text=dataDetails.about
        address_providerDetail.text=dataDetails.contactInformation.address
        slider_providerDetail.setItems(dataDetails.images)

        whatsapp_providerDetail.setOnClickListener {
            Constants.openWhatsApp(this,whatsapp_providerDetail.text.toString())
        }
    }
    fun onWhatsappClicked(v: View){
        val whatappNumber=v.whatsapp_providerDetail.text.toString()
        if(whatappNumber.isNotEmpty())
        Constants.openWhatsApp(this,whatappNumber)

        PrintMsg.toastDebug(this,"onWhatsappClicked")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
