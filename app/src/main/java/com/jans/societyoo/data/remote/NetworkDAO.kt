package com.jans.societyoo.data.remote

import com.jans.societyoo.model.*
import com.jans.societyoo.model.login.*
import com.jans.societyoo.model.login.UserData
import com.jans.societyoo.model.post.CreatePost
import com.jans.societyoo.model.services.ProviderDetail
import com.jans.societyoo.model.services.ProviderPost
import com.jans.societyoo.model.services.Services
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

const val BASE_URL_IMGUR = "https://appfeedlight.bhaskar.com/appFeedV3/"
const val BASE_URL_TEST = "https://jsonplaceholder.typicode.com"
const val BASE_URL = "https://myapartment.janstechnologies.com/societyooapi/public/api/" //"http://34.93.172.52/societyooapi/public/api/"

interface JsonApi {

    @POST("getotp")
    suspend fun sendOTP(@Body otpRequest: OtpRequest): ApiDataWithOutObject

    @POST("validotp")
    suspend fun verifyOTP(@Body otpVerifyRequest: OtpVerifyRequest): ApiDataObject<UserData>

    @POST("inseruserdetails")
    suspend fun updateUserProfile(@Body userDetail: UserDetail): ApiDataObject<UserData>




    @GET("getallservices")
    suspend fun getAllServices(): ApiDataObject<Services>

    @GET("getservices/{id}")
    suspend fun getSocietyServices(@Path(value = "id") societyId: Int): ApiDataObject<Services>

    @GET("getprovider/{id}")
    suspend fun getProviderDetail(@Path(value = "id") providerID: Int): ApiDataObject<ProviderDetail>

    @POST("insertprovider")
    suspend fun postProviderDetail(@Body providerPost: ProviderPost): ApiDataWithOutObject

    @POST("submitpost")
    suspend fun insertPost(@Body createPost: CreatePost): ApiDataWithOutObject



    @Multipart
    @POST("uploadfile")
    fun uploadFile(@Part file: MultipartBody.Part,@PartMap map: HashMap<String,RequestBody>): Call<ApiDataFile>

}