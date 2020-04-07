package com.jans.societyoo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
/*class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(*//*
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource()
                )*//*
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/

class PostProviderViewModelFactory(private val context : Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostProviderViewModel::class.java)) {
            return PostProviderViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
