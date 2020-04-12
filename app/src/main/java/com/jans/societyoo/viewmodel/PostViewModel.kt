package com.jans.societyoo.viewmodel


import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jans.societyoo.data.repository.DataRepository
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.model.post.CreatePost
import com.jans.societyoo.model.post.Post
import com.jans.societyoo.model.services.ProviderPost
import com.jans.societyoo.utils.PrintMsg
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

public class PostViewModel(context: Context) : ViewModel() {
    val context: Context = context
    val userDetailLiveData = MutableLiveData<UserDetail>()

    private val dataRepository: DataRepository = DataRepository(context);

    fun getUserDetailDB() {
        GlobalScope.launch {
            val result = dataRepository.getUserDetailDB()
            userDetailLiveData.postValue(result)
            if (result != null) {
                PrintMsg.println("Room DB : getUserDetailDB : " + result.toString())
            }
        }
    }

    fun insertPost(createPost: CreatePost) = liveData {
        val result = dataRepository.insertPost(createPost)
        PrintMsg.println("API Response : submitCreatePost : ${result.toString()}")
        emit(result)
    }


    private fun checkPost(text: String): Boolean {
        if (!TextUtils.isEmpty(text))
            return true
        return false
    }

    private fun checkImages(imageList: List<String>): Boolean {
        if (imageList.isNotEmpty())
            return true
        return false
    }

    fun validPostData(post: Post): Boolean {
        val validPost = checkPost(post.desc)
        val validImage = checkImages(post.images)
        return validPost || validImage
    }


}