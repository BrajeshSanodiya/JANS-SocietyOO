package com.my.retrodemo1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import retrofit2.Response

class LoginViewModel() : ViewModel() {
    val userRepository = UserRepository()

    fun getSingleAlbum(id: Int) = liveData {
        val result = userRepository.getUser(id)
        emit(result)
    }

    /*fun getSingleAlbumResponse(id: Int) = liveData {
        val receivedAlbumResponse = albumsRepository.getAlbum(id)
        if (receivedAlbumResponse is Result.Success) {
            eventManager.stopProgressBar()
            eventManager.viewToast("Successfully Done")
            emit(receivedAlbumResponse.data)
        } else {
            eventManager.viewToast("Sorry Failed")
        }
        eventManager.stopProgressBar()
    }*/
}