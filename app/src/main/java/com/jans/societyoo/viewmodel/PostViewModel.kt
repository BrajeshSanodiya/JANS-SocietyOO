package com.jans.societyoo.viewmodel


import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.jans.societyoo.data.local.prefs.UserPreferences
import com.jans.societyoo.data.repository.DataRepository
import com.jans.societyoo.model.login.UserDetail
import com.jans.societyoo.model.post.Post
import com.jans.societyoo.model.services.MicroService
import com.jans.societyoo.model.services.ProviderDetail
import com.jans.societyoo.model.services.ProviderPost
import com.jans.societyoo.ui.services.PostProviderViewState
import com.jans.societyoo.utils.MyResult
import com.jans.societyoo.utils.PrintMsg
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

public class PostViewModel(context: Context) : ViewModel() {

    val userDetailLiveData = MutableLiveData<UserDetail>()

    private val dataRepository: DataRepository = DataRepository(context);

    fun getAllServices() = liveData {
        val result=dataRepository.getAllServices()
        PrintMsg.println("API Response : getAllServices : ${result.toString()}")
        emit(result)
    }

    fun getUserDetailDB() {
        GlobalScope.launch {
            val result = dataRepository.getUserDetailDB()
            userDetailLiveData.postValue(result)
            if (result != null) {
                PrintMsg.println("Room DB : getUserDetailDB : " + result.toString())
            }
        }
    }

    fun getFilterMicroServices(serviceId: Int,list: List<MicroService>) = liveData {
        val result=ArrayList<MicroService>()
        for (item in list){
          if(item.serviceId==serviceId)
              result.add(item)
        }

        PrintMsg.println("Filter Data : getFilterMicroServices : ${result.toString()}")
        emit(result)
    }


    fun postProviderDetail(providerPost: ProviderPost) = liveData {
        val result = dataRepository.postProviderDetail(providerPost)
        PrintMsg.println("API Response : getProviderDetail : ${result.toString()}")
        emit(result)
    }


    private fun checkPost(text: String): Boolean {
        if (!TextUtils.isEmpty(text))
            return true
        return false
    }

    fun validPostData(post: Post/*title:String,webUrl:String*/): Boolean {
        val validPost=checkPost(post.desc)
        return validPost
    }
}