package com.jans.societyoo.data.repository

import android.content.Context
import com.jans.societyoo.data.local.db.DatabaseDataSource
import com.jans.societyoo.data.remote.NetworkDataSource
import com.jans.societyoo.model.*
import com.jans.societyoo.model.login.*
import com.jans.societyoo.model.login.UserData
import com.jans.societyoo.model.services.*
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.SingleRunner

class DataRepository(context: Context) {

    var databaseDataSource:DatabaseDataSource= DatabaseDataSource(context)

    var networkDataSource: NetworkDataSource = NetworkDataSource()

    val singleRunner = SingleRunner()
    suspend fun sendOtp(otpRequest: OtpRequest): MyResult<ApiDataObject<SendOTPData>>{
        return networkDataSource.sendOTP(otpRequest)
    }

    suspend fun getAllFlatsDB(): List<FlatDetail> {
            return databaseDataSource.getAllFlats()
    }
    suspend fun getDefaultFlatSocietyId(defaultUserId:Int): Int {
        return databaseDataSource.getDefaultFlatSocietyId(defaultUserId)
    }
    suspend fun getUserDetailDB(): UserDetail {
        return databaseDataSource.getUserDetail()
    }

    suspend fun setUserDetailDB(userDetail: UserDetail) {
        return databaseDataSource.insertUserDetail(userDetail)
    }
    suspend fun deleteAllUsersDB() {
        return databaseDataSource.deleteAllUsers()
    }

    suspend fun deleteAllFlatsDB() {
        return databaseDataSource.deleteAllFlats()
    }

    suspend fun setFlatDetailsDB(flatDetails: List<FlatDetail>){
        return databaseDataSource.insertAllFlats(flatDetails)
    }

    suspend fun verifyOtp(otpVerifyRequest: OtpVerifyRequest): MyResult<ApiDataObject<UserData>>{
        var result:MyResult<ApiDataObject<UserData>> =  networkDataSource.verifyOTP(otpVerifyRequest)
        return result
    }

    suspend fun updateUserProfile(userDetail: UserDetail): MyResult<ApiDataObject<UserData>>{
       var result:MyResult<ApiDataObject<UserData>> =  networkDataSource.updateUserProfile(userDetail)
        return result
    }


    // Dashboard Services list
    suspend fun getDashboardServices(societyId: Int): MyResult<ApiDataObject<Services>>{
        return networkDataSource.getDashboardServices(societyId)
    }

    suspend fun deleteAllServiceDB() {
        return databaseDataSource.deleteAllService()
    }
    suspend fun deleteAllMicroServiceDB() {
        return databaseDataSource.deleteAllMicroService()
    }
    suspend fun deleteAllServiceProviderDB() {
        return databaseDataSource.deleteAllServiceProvider()
    }
    suspend fun getAllServiceDB(): List<Service> {
        return databaseDataSource.getAllService()
    }
    suspend fun getAllMicroServiceDB(serviceId: Int): List<MicroService> {
        return databaseDataSource.getAllMicroService(serviceId)
    }
    suspend fun getAllServiceProviderDB(microServiceId: Int): List<Provider> {
        return databaseDataSource.getAllServiceProvider(microServiceId)
    }

    suspend fun setAllServiceDB(serviceList: List<Service>) {
        return databaseDataSource.insertAllService(serviceList)
    }
    suspend fun setAllMicroServiceDB(microServiceList: List<MicroService>) {
        return databaseDataSource.insertAllMicroService(microServiceList)
    }
    suspend fun setAllServiceProviderDB(providerList: List<Provider>) {
        return databaseDataSource.insertAllServiceProvider(providerList)
    }
    suspend fun getProviderDetail(providerId: Int): MyResult<ApiDataObject<ProviderDetail>>{
        return networkDataSource.getProviderDetail(providerId)
    }
    suspend fun postProviderDetail(providerPost: ProviderPost): MyResult<ApiDataWithOutObject>{
        return networkDataSource.postProviderDetail(providerPost)
    }

}

