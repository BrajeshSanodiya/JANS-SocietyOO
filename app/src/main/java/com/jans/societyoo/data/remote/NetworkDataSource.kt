package com.jans.societyoo.data.remote

import android.text.TextUtils
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.ApiDataObjectWithCursor
import com.jans.societyoo.model.ApiDataWithOutObject
import com.jans.societyoo.model.login.*
import com.jans.societyoo.model.post.CreatePost
import com.jans.societyoo.model.post.Post
import com.jans.societyoo.model.services.ProviderDetail
import com.jans.societyoo.model.services.ProviderPost
import com.jans.societyoo.model.services.Services
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.tryCatching

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class NetworkDataSource {

    var jsonServices: JsonApi = NetworkInstance.jsonServices
    var jsonServicesTest: JsonApi = NetworkInstance.jsonServicesTest
    var jsonServicesImgUr:JsonApi = NetworkInstance.jsonServicesImgUr

    suspend fun sendOTP(otpRequest: OtpRequest): MyResult<ApiDataWithOutObject> = tryCatching {
        jsonServices.sendOTP(otpRequest)
    }
    suspend fun verifyOTP(otpVerifyRequest: OtpVerifyRequest): MyResult<ApiDataObject<UserData>> = tryCatching {
        jsonServices.verifyOTP(otpVerifyRequest)
    }
    suspend fun updateUserProfile(userDetail: UserDetail): MyResult<ApiDataObject<UserData>> = tryCatching {
        jsonServices.updateUserProfile(userDetail)
    }

    suspend fun getAllServices(): MyResult<ApiDataObject<Services>> = tryCatching {
        jsonServices.getAllServices()
    }
    suspend fun getSocietyServices(societyId: Int): MyResult<ApiDataObject<Services>> = tryCatching {
        jsonServices.getSocietyServices(societyId)
    }
    suspend fun getProviderDetail(providerId: Int): MyResult<ApiDataObject<ProviderDetail>> = tryCatching {
        jsonServices.getProviderDetail(providerId)
    }
    suspend fun postProviderDetail(providerPost: ProviderPost): MyResult<ApiDataWithOutObject> = tryCatching {
        jsonServices.postProviderDetail(providerPost)
    }

    suspend fun insertPost(createPost: CreatePost): MyResult<ApiDataWithOutObject> = tryCatching {
        jsonServices.insertPost(createPost)
    }
    suspend fun getPostList(socityId:Int): MyResult<ApiDataObjectWithCursor<List<Post>>> = tryCatching {
        jsonServices.getPostList(socityId)
    }
    suspend fun getPostList(socityId:Int,cursor:String): MyResult<ApiDataObjectWithCursor<List<Post>>> = tryCatching {
        jsonServices.getPostList(socityId,cursor)
    }
}

