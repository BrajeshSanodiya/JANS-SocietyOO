package com.jans.societyoo.data.remote

import com.jans.societyoo.model.*
import com.jans.societyoo.model.login.OTPVerifyData
import com.jans.societyoo.model.login.OtpRequest
import com.jans.societyoo.model.login.OtpVerifyRequest
import com.jans.societyoo.model.login.SendOTPData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

const val BASE_URL_TEST = "https://jsonplaceholder.typicode.com"
const val BASE_URL = "https://myapartment.janstechnologies.com/societyooapi/public/api/"

interface JsonApi {

    @POST("getotp")
    suspend fun sendOTP(@Body otpRequest: OtpRequest): ApiDataObject<SendOTPData>

    @POST("validotp")
    suspend fun verifyOTP(@Body otpVerifyRequest: OtpVerifyRequest): ApiDataObject<OTPVerifyData>


    @GET("/users/{id}")
    suspend fun getUser(@Path(value = "id") userId: Int): User

    @GET("/users")
    suspend fun getUserList(): List<User>

    @POST("/posts")
    suspend fun postUserData(@Body userPostData: UserPostData): UserData
}