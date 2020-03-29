package com.jans.societyoo.ui.societyservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jans.societyoo.R
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.main.ProviderDetail
import com.jans.societyoo.utils.Constants
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import com.jans.societyoo.viewmodel.SocietyServicesViewModel
import com.jans.societyoo.viewmodel.SocityServicesViewModelFactory
import kotlinx.android.synthetic.main.activity_service_provider_detail.*

class ServiceProviderDetailActivity : AppCompatActivity() {
    lateinit var viewModel: SocietyServicesViewModel
    private var providerID: Int = 0
    private var providerHeaderTitle: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_provider_detail)

        if (intent.extras != null) {
            providerID = intent.extras!!.getInt("provider_id")
            providerHeaderTitle = intent.extras!!.getString("provider_headerTitle")!!
        }

        supportActionBar!!.setTitle(providerHeaderTitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);

        viewModel = ViewModelProvider(viewModelStore, SocityServicesViewModelFactory(this)).get(
            SocietyServicesViewModel::class.java
        )


        // add images URLs
       /* val images = listOf(
            "https://www.ecopetit.cat/wpic/mpic/2-22781_1080p-hd-wallpapers-data-src-andro-mobile-android.jpg",
            "https://lh4.googleusercontent.com/proxy/-qKKZa52bNtPRwa88VRzYYwzKZSgyrStJoKBWu8vFsUbt7IjaF5X7TNNOKKKxzaUW8yf1EjlTFMGai5nKdZbf3VP3cIzFI16jmI1yx-0Rt_w-4hDTIflfdtmrxDUd6axauY",
            "https://i.pinimg.com/originals/ac/c6/b4/acc6b48619b3cba02b613006f82470c9.gif",
            "https://www.jakpost.travel/imgfiles/full/7/78215/live-moving-wallpaper-for-mobile.gif",
            "https://thehighdefinitionwallpapers.com/wp-content/uploads/2020/03/AbstractAbstract-Black-and-green-square-wall-3D-HD-wallpaper-1136x640.jpg",
            "https://www.ecopetit.cat/wpic/mpic/1-18403_wallpapers-for-android-mobile-free-download-android-wallpaper.jpg",
            "https://wallpapercave.com/wp/wp3439083.jpg",
            "https://wallpaperaccess.com/full/57192.jpg",
            "https://3dandroidwallpaper.com/wp-content/uploads/2019/10/Android-Wallpaper-HD-Marshmello.jpg",
            "https://www.elsetge.cat/myimg/f/0-4010_31-full-hd-mobile-wallpapers-for-samsung-nokia.png",
            "https://wallpaperplay.com/walls/full/7/1/9/210867.jpg",
            "https://wallpapercave.com/wp/wp2875661.jpg"
            )*/
        // Add image URLs to sliderImage
          //slider_providerDetail.setItems(images)
        //slider_providerDetail.addTimerToSlide(5000)
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
                        bindUIData(data.data_details)

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
