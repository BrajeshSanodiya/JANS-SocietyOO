package com.jans.societyoo.data.local.db

import android.content.Context
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.model.main.MicroService
import com.jans.societyoo.model.main.Provider
import com.jans.societyoo.model.main.Service

class DatabaseDataSource(context: Context) {
    var dbServices: DatabaseDAO = DatabaseInstance.getInstance(context).databaseDAO

    suspend fun insertAllFlats(flatDetails: List<FlatDetail>){
        return dbServices.insertAllFlats(flatDetails)
    }

    suspend fun getAllFlats(): List<FlatDetail> {
       return dbServices.getAllFlats()
    }
    suspend fun getDefaultFlatSocietyId(defaultUserId:Int): Int {
        return dbServices.getDefaultFlatSocietyId(defaultUserId)
    }

    suspend fun deleteAllFlats(){
        return dbServices.deleteAllFlats()
    }
    suspend fun deleteAllUsers(){
        return dbServices.deleteAllUsers()
    }
    suspend fun insertUserDetail(userDetail: UserDetail){
        return dbServices.insertUserDetail(userDetail)
    }

    suspend fun getUserDetail(): UserDetail {
        return dbServices.getUserDetail()
    }


    suspend fun deleteAllService(){
        return dbServices.deleteAllService()
    }
    suspend fun deleteAllMicroService(){
        return dbServices.deleteAllMicroService()
    }
    suspend fun deleteAllServiceProvider(){
        return dbServices.deleteAllServiceProvider()
    }
    suspend fun getAllService(): List<Service> {
        return dbServices.getAllService()
    }
    suspend fun getAllMicroService(serviceId:Int): List<MicroService> {
        return dbServices.getMicroService(serviceId)
    }
    suspend fun getAllServiceProvider(microServiceId:Int): List<Provider> {
        return dbServices.getServiceProvider(microServiceId)
    }
    suspend fun insertAllService(serviceList: List<Service>){
        return dbServices.insertAllService(serviceList)
    }
    suspend fun insertAllMicroService(microServiceList: List<MicroService>){
        return dbServices.insertAllMicroService(microServiceList)
    }
    suspend fun insertAllServiceProvider(providerList: List<Provider>){
        return dbServices.insertAllServiceProvider(providerList)
    }
}