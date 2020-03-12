package com.jans.societyoo.data.local.db

import android.content.Context
import androidx.lifecycle.LiveData
import com.jans.societyoo.data.remote.JsonApi
import com.jans.societyoo.data.remote.RetrofitInstance
import com.jans.societyoo.model.ApiDataObject
import com.jans.societyoo.model.login.FlatDetail
import com.jans.societyoo.model.login.OtpRequest
import com.jans.societyoo.model.login.SendOTPData
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.tryCatching

class LoginDataSourceDB(context: Context) {
    var dbServices: DatabaseDAO = DatabaseInstance.getInstance(context).databaseDAO

    suspend fun insertAllFlats(flatDetails: List<FlatDetail>){
        return dbServices.insertAllFlats(flatDetails)
    }

    suspend fun getAllFlats(): List<FlatDetail> {
       return dbServices.getAllFlats()
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


}