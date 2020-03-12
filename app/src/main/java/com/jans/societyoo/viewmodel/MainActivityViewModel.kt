package com.jans.societyoo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jans.societyoo.model.UserPostData
import com.jans.societyoo.data.repository.LoginRepository

class MainActivityViewModel(context: Context) : ViewModel() {
    val userRepository = LoginRepository(context)

    fun getUser(id: Int) = liveData {
        val result = userRepository .getUser(id)
        println("API Response : postUser : ${result.toString()}")
        emit(result)
    }

    fun getUserList() = liveData {
        val result= userRepository .getUserList()
        println("API Response : postUser : ${result.toString()}")
        emit(result)
    }

    fun postUser(postData: UserPostData) = liveData {
        val result= userRepository.postUserData(postData)
        println("API Response : postUser : ${result.toString()}")
        emit(result)
    }
}