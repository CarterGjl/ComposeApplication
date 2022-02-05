package com.example.composeapplication.ui.screen.type.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.composeapplication.bean.Article
import com.example.composeapplication.model.RemoteSevice
import com.example.composeapplication.viewmodel.BaseViewModel
import kotlinx.coroutines.async

private const val TAG = "TypeContentViewModel"

class TypeContentViewModel : BaseViewModel() {

    val articalLiveData = MutableLiveData<List<Article>>()

    var currentId = 0
    fun getArticleList(page: Int = 0, cid: Int) {
        if (currentId == cid) {
            return
        }
        currentId = cid
        launch {
            Log.d(TAG, "getArticleList: $cid")
            val async = viewModelScope.async {
                RemoteSevice.getInstance().getArticleList(page = page, cid = cid)
            }
            val result = async.await()
            articalLiveData.value = result.data.datas
        }
    }
}