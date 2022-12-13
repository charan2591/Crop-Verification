package org.agripmu.cropverification.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.agripmu.cropverification.models.MainModel

class MainViewModel : ViewModel() {

    var list = MutableLiveData<ArrayList<MainModel>>()
    var newList = arrayListOf<MainModel>()

    fun add(model : MainModel)
    {
        newList.add(model)
        list.value = newList
    }

    fun remove(model : MainModel)
    {
        newList.remove(model)
        list.value = newList
    }
}