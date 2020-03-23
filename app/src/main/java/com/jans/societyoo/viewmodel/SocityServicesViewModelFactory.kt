package com.jans.societyoo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SocityServicesViewModelFactory(private val context : Context) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SocietyServicesViewModel::class.java)) {
            return SocietyServicesViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
