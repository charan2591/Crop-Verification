package org.agripmu.cropverification.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.agripmu.cropverification.viewmodels.MainViewModel

class MainViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java))
        {
            return MainViewModel() as T
        }

        throw IllegalArgumentException("UnknownViewModel")
    }

//    fun <T : ViewModel?> create(modelClass: Class<T>): T {
//
//        if(modelClass.isAssignableFrom(MainViewModel::class.java))
//        {
//            return MainViewModel() as T
//        }
//
//        throw IllegalArgumentException("UnknownViewModel")
//    }
}