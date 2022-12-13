package org.agripmu.cropverification.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.agripmu.cropverification.models.SearchModel

class SearchViewModel : ViewModel()  {

    var list = MutableLiveData<ArrayList<SearchModel>>()
    var newList = arrayListOf<SearchModel>()

    fun add(model : SearchModel)
    {
        newList.add(model)
        list.value = newList
    }
}