package com.jans.societyoo.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jans.societyoo.data.repository.DataRepository
import com.jans.societyoo.model.services.Services
import com.jans.societyoo.utils.PrintMsg
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DashboardActivityViewModel(context: Context) : ViewModel() {
    val dataRepository = DataRepository(context)
    val socityIdLiveData = MutableLiveData<Int>()

    fun getSocietyServices(societyId: Int) = liveData {
            val result = dataRepository .getSocietyServices(societyId)
            PrintMsg.println("API Response : getDashboardServices : ${result.toString()}")
            emit(result)
    }

    fun getDashboardServicesDB() = liveData {
        val result=dataRepository.getAllServiceDB()
        PrintMsg.println("Room DB : getDashboardServicesDB : ${result.toString()}")
        emit(result)
    }

    fun getPostList(societyId: Int) = liveData {
        val result = dataRepository.getPostList(societyId)
        PrintMsg.println("API Response : getDashboardServices : ${result.toString()}")
        emit(result)
    }

    fun getPostList(societyId: Int,cursor:String) = liveData {
        val result = dataRepository.getPostList(societyId,cursor)
        PrintMsg.println("API Response : getPostList : ${result.toString()}")
        emit(result)
    }


    fun getSocityIdDB() {
     GlobalScope.launch {
         var userDetail = dataRepository.getUserDetailDB()
         if (userDetail != null && userDetail.defaultUserId != null && userDetail.defaultUserId != 0) {
             val result = dataRepository.getDefaultFlatSocietyId(userDetail.defaultUserId!!)
             PrintMsg.println("Room DB : getSocityIdDB : ${result.toString()}")
             socityIdLiveData.postValue(result)
         } else {
             socityIdLiveData.postValue(0)
         }
     }
    }

    fun setDashboardServicesDB(services: Services) {
        GlobalScope.launch {
            var serviceDeleted = dataRepository.deleteAllServiceDB()
            PrintMsg.println("Room DB : setUserDetailDB : serviceDeleted->" + serviceDeleted.toString())
            var microServiceDeleted = dataRepository.deleteAllMicroServiceDB()
            PrintMsg.println("Room DB : setUserDetailDB : MicroServiceDeleted->" + microServiceDeleted.toString())
            var providerDeleted = dataRepository.deleteAllServiceProviderDB()
            PrintMsg.println("Room DB : setUserDetailDB : ProviderDeleted->" + providerDeleted.toString())

            if (services != null) {
                if(services.services!=null && services.services.size>0){
                    var resultadded = dataRepository.setAllServiceDB(services.services)
                    PrintMsg.println("Room DB : setUserDetailDB : resultAdded->" + resultadded.toString())
                }
                if(services.microServices!=null && services.microServices!!.size>0){
                    var resultadded = dataRepository.setAllMicroServiceDB(services.microServices!!)
                    PrintMsg.println("Room DB : setUserDetailDB : resultAdded->" + resultadded.toString())
                }
                if(services.providers!=null && services.providers!!.size>0){
                    var resultadded = dataRepository.setAllServiceProviderDB(services.providers!!)
                    PrintMsg.println("Room DB : setUserDetailDB : resultAdded->" + resultadded.toString())
                }
            }
        }
    }

}