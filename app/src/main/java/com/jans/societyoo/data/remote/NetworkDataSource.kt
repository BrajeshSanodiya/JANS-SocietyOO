package com.jans.societyoo.data.remote

import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.User
import com.jans.societyoo.model.login.*
import com.jans.societyoo.model.main.ProviderDetail
import com.jans.societyoo.model.main.Services
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.tryCatching

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class NetworkDataSource {

    var jsonServices: JsonApi = NetworkInstance.jsonServices
    var jsonServicesTest: JsonApi = NetworkInstance.jsonServicesTest

    suspend fun sendOTP(otpRequest: OtpRequest): MyResult<ApiDataObject<SendOTPData>> = tryCatching {
        jsonServices.sendOTP(otpRequest)
    }
    suspend fun verifyOTP(otpVerifyRequest: OtpVerifyRequest): MyResult<ApiDataObject<UserData>> = tryCatching {
        jsonServices.verifyOTP(otpVerifyRequest)
    }

    suspend fun updateUserProfile(userDetail: UserDetail): MyResult<ApiDataObject<UserData>> = tryCatching {
        jsonServices.updateUserProfile(userDetail)
    }
    suspend fun getDashboardServices(societyId: Int): MyResult<ApiDataObject<Services>> = tryCatching {
        jsonServices.getDashboardServices(societyId)
    }
    suspend fun getProviderDetail(providerId: Int): MyResult<ApiDataObject<ProviderDetail>> = tryCatching {
        jsonServices.getProviderDetail(providerId)
    }


    suspend fun getUser(userId: Int): MyResult<User> = tryCatching {
        jsonServicesTest.getUser(userId)
    }

    suspend fun getUserList(): MyResult<List<User>> = tryCatching {
        jsonServicesTest.getUserList()
    }
}

