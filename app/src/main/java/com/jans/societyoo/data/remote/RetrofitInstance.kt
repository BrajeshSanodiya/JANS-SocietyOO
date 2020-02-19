package com.my.retrodemo1.retrofit

import com.google.gson.GsonBuilder
import com.jans.societyoo.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    val jsonServices by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .client(makeHttpClient(/*accessTokenProvidingInterceptor()*/))
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(JsonApi::class.java)
    }

    fun makeHttpClient(/*interceptors: Interceptor*/) = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(headersInterceptor())
        //.apply { interceptors().add(interceptors) }
        .addInterceptor(loggingInterceptor())
        .build()

    fun accessTokenProvidingInterceptor() = Interceptor { chain ->
        val accessToken = "" //Pref.token
        chain.proceed(chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build())
    }

    fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }


    fun headersInterceptor() = Interceptor { chain ->
        chain.proceed(chain.request().newBuilder()
            //.addHeader("Accept", "application/json")
            //.addHeader("Accept-Language", "en")
            .addHeader("Content-Type", "application/json; charset=UTF-8")

            .build())
    }
   }