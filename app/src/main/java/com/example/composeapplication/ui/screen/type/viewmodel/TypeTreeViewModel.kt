package com.example.composeapplication.ui.screen.type.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.composeapplication.model.RemoteSevice
import com.example.composeapplication.ui.screen.type.bean.TreeListResponse
import com.example.composeapplication.viewmodel.BaseViewModel
import kotlinx.coroutines.async

class TypeTreeViewModel : BaseViewModel() {

    val knowledges = MutableLiveData<List<TreeListResponse.Knowledge>>()

    fun getTreeResponse() {
        launch {
            val async = viewModelScope.async {
                RemoteSevice.getInstance().getTypeTreeList()
            }
            val result = async.await()
            knowledges.value = result.data

        }

    }
}