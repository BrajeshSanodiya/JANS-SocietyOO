package com.jans.societyoo.viewmodel

import android.app.Service
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jans.societyoo.data.repository.DataRepository
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.model.main.MicroService
import com.jans.societyoo.model.main.Provider
import com.jans.societyoo.model.main.Services
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SocietyServicesViewModel(context: Context) : ViewModel() {
    val dataRepository = DataRepository(context)
    val microServiceLiveData = MutableLiveData<List<MicroService>>()
    val serviceProviderLiveData = MutableLiveData<List<Provider>>()

    fun getProviderDetail(providerId: Int) = liveData {
            val result = dataRepository.getProviderDetail(providerId)
            PrintMsg.println("API Response : getProviderDetail : ${result.toString()}")
            emit(result)
    }

    fun getMicroServicesDB(serviceId: Int) {
        GlobalScope.launch {
            val result = dataRepository.getAllMicroServiceDB(serviceId)
            PrintMsg.println("Room DB : getMicroServicesDB : ${result.toString()}")
            microServiceLiveData.postValue(result)
        }
    }

    fun getServiceProviderDB(microServiceId: Int) {
        GlobalScope.launch {
            val result = dataRepository.getAllServiceProviderDB(microServiceId)
            PrintMsg.println("Room DB : getMicroServicesDB : ${result.toString()}")
            serviceProviderLiveData.postValue(result)
        }
    }
}


