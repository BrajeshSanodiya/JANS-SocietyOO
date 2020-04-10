package com.jans.societyoo.viewmodel


import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.data.repository.DataRepository
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.model.services.MicroService
import com.jans.societyoo.model.services.ProviderDetail
import com.jans.societyoo.model.services.ProviderPost
import com.jans.societyoo.ui.services.PostProviderViewState
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

public class PostProviderViewModel(context: Context) : ViewModel() {

    val postProviderViewState = MutableLiveData<PostProviderViewState>()
    val userDetailLiveData = MutableLiveData<UserDetail>()

    private val dataRepository: DataRepository = DataRepository(context);

    fun getAllServices() = liveData {
        val result=dataRepository.getAllServices()
        PrintMsg.println("API Response : getAllServices : ${result.toString()}")
        emit(result)
    }

    fun getUserDetailDB() {
        GlobalScope.launch {
            val result = dataRepository.getUserDetailDB()
            userDetailLiveData.postValue(result)
            if (result != null) {
                PrintMsg.println("Room DB : getUserDetailDB : " + result.toString())
            }
        }
    }

    fun getFilterMicroServices(serviceId: Int,list: List<MicroService>) = liveData {
        val result=ArrayList<MicroService>()
        for (item in list){
          if(item.serviceId==serviceId)
              result.add(item)
        }

        PrintMsg.println("Filter Data : getFilterMicroServices : ${result.toString()}")
        emit(result)
    }


    fun postProviderDetail(providerPost: ProviderPost) = liveData {
        val result = dataRepository.postProviderDetail(providerPost)
        PrintMsg.println("API Response : getProviderDetail : ${result.toString()}")
        emit(result)
    }



//    fun getMicroServicesDB(serviceId: Int) {
//        GlobalScope.launch {
//            val result = dataRepository.getAllMicroServiceDB(serviceId)
//            PrintMsg.println("Room DB : getMicroServicesDB : ${result.toString()}")
//            microServiceLiveData.postValue(result)
//        }
//    }


    private fun checkLogo(url: String): Boolean {
        if (Patterns.WEB_URL.matcher(url).matches())
            return true
        return false
    }

    private fun checkTitle(name: String): Boolean {
        if (name.length > 3)
            return true
        return false
    }

    private fun checkImages(imageList: List<String>): Boolean {
        if (imageList.isNotEmpty())
            return true
        return false
    }

    private fun checkWebsite(url: String): Boolean {
        if (Patterns.WEB_URL.matcher(url).matches())
            return true
        return false
    }

    private fun checkTime(text: String): Boolean {
        if (!TextUtils.isEmpty(text) && text.length > 3)
            return true
        return false
    }

    private fun checkAbout(text: String): Boolean {
        if (!TextUtils.isEmpty(text) && text.length > 5)
            return true
        return false
    }

    private fun checkCategory(id: Int): Boolean {
        if (id > 0)
            return true
        return false
    }

    private fun checkSubCategory(id: Int): Boolean {
        if (id > 0)
            return true
        return false
    }

    private fun checkPersonName(name: String): Boolean {
        if (name.length > 3)
            return true
        return false
    }

    private fun checkEmail(email: String): Boolean {
        if (email.contains('@') && Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true
        return false
    }

    private fun checkMobile(mobile: String): Boolean {
        if (mobile.length >= 10){
            try {
                val phoneInstance = PhoneNumberUtil.getInstance()
                val phoneNumber = phoneInstance.parse(mobile, "IN")
                val isValid: Boolean = phoneInstance.isValidNumber(phoneNumber);
                if(isValid)
                    return true
                return false
            } catch (e: Throwable) {
                return false
            }
        }
        return false
    }

    private fun checkWhatsapp(whatsapp: String): Boolean {
        if (whatsapp.length >= 10){
            try {
                val phoneInstance = PhoneNumberUtil.getInstance()
                val phoneNumber = phoneInstance.parse(whatsapp, "IN")
                val isValid: Boolean = phoneInstance.isValidNumber(phoneNumber);
                if(isValid)
                    return true
                return false
            } catch (e: Throwable) {
                return false
            }
        }
        return false
    }


    fun validPostData(providerPost: ProviderPost/*title:String,webUrl:String*/): Boolean {
        val validLogo=checkLogo(providerPost.providerDetail.logo)
        val validTitle=checkTitle(providerPost.providerDetail.serviceTitle)
        val validImage=checkImages(providerPost.providerDetail.images)
        val validWebsite =checkWebsite(providerPost.providerDetail.contactInformation.website)

        val validTime =checkTime(providerPost.providerDetail.contactInformation.workingHours)
        val validAbout =checkAbout(providerPost.providerDetail.about)
        val validCategory =checkCategory(providerPost.service_id)
        val validSubCategory =checkSubCategory(providerPost.micro_service_id)
        val validPerson =checkPersonName(providerPost.providerDetail.contactInformation.contactPerson)
        val validEmail=checkEmail(providerPost.providerDetail.contactInformation.email)
        val validPhone=checkMobile(providerPost.providerDetail.contactInformation.phone)
        val validWhatsApp= checkWhatsapp(providerPost.providerDetail.contactInformation.whatsApp)

        postProviderViewState.value= PostProviderViewState(
            validLogo=validLogo,
            validServiceTitle = validTitle,
            validImages = validImage,
            validWebsite = validWebsite,
            validTime = validTime,
            validAbout = validAbout,
            validCategory = validCategory,
            validSubCategory = validSubCategory,
            validPerson = validPerson,
            validEmail = validEmail,
            validMobile = validPhone,
            validWhatsapp = validWhatsApp
        )
        return validLogo && validTitle && validImage && validWebsite && validTime && validAbout && validCategory && validSubCategory && validPerson && validEmail && validPhone && validWhatsApp



        /*if (!checkLogo(providerPost.providerDetail.logo)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = false
            )
            return false
        }
        if (!checkTitle(providerPost.providerDetail.serviceTitle)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = false
            )
            return false
        }
        if (!checkImages(providerPost.providerDetail.images)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = false
            )
            return false
        }
        if (!checkWebsite(providerPost.providerDetail.contactInformation.website)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = true,
                validWebsite = false
            )
            return false
        }
        if (!checkTime(providerPost.providerDetail.contactInformation.workingHours)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = true,
                validWebsite = true,
                validTime = false
            )
            return false
        }
        if (!checkAbout(providerPost.providerDetail.about)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = true,
                validWebsite = true,
                validTime = true,
                validAbout = false
            )
            return false
        }
        if (!checkCategory(providerPost.service_id)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = true,
                validWebsite = true,
                validTime = true,
                validAbout = true,
                validCategory = false
            )
            return false
        }

        if (!checkSubCategory(providerPost.micro_service_id)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = true,
                validWebsite = true,
                validTime = true,
                validAbout = true,
                validCategory = true,
                validSubCategory = false
            )
            return false
        }

        if (!checkPersonName(providerPost.providerDetail.contactInformation.contactPerson)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = true,
                validWebsite = true,
                validTime = true,
                validAbout = true,
                validCategory = true,
                validSubCategory = true,
                validPerson = false
            )
            return false
        }

        if (!checkEmail(providerPost.providerDetail.contactInformation.email)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = true,
                validWebsite = true,
                validTime = true,
                validAbout = true,
                validCategory = true,
                validSubCategory = true,
                validPerson = true,
                validEmail = false
            )
            return false
        }

        if (!checkMobile(providerPost.providerDetail.contactInformation.phone)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = true,
                validWebsite = true,
                validTime = true,
                validAbout = true,
                validCategory = true,
                validSubCategory = true,
                validPerson = true,
                validEmail = true,
                validMobile = false
            )
            return false
        }
        if (!checkWhatsapp(providerPost.providerDetail.contactInformation.whatsApp)) {
            postProviderViewState.value = PostProviderViewState(
                validLogo = true,
                validServiceTitle = true,
                validImages = true,
                validWebsite = true,
                validTime = true,
                validAbout = true,
                validCategory = true,
                validSubCategory = true,
                validPerson = true,
                validEmail = true,
                validMobile = true,
                validWhatsapp = false
            )
            return false
        }

        return true*/
    }
}