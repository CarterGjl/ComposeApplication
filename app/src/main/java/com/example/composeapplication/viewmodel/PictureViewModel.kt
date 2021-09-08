package com.example.composeapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.composeapplication.bean.PictureModel
import com.example.composeapplication.model.Http
import com.example.composeapplication.viewmodel.picture.PictureSource
import kotlinx.coroutines.flow.Flow

private const val TAG = "PictureViewModel"

class PictureViewModel :BaseViewModel() {
    val picLiveData = MutableLiveData<List<PictureModel>>()

    fun getPicList() {
        launch {
            val pics = Http.getInstance().gank.getPics1().data
            Log.d(TAG, "getPicList: $pics")
        }
    }

    val pics: Flow<PagingData<PictureModel>> = Pager(
        PagingConfig(pageSize = 21)
    ) {
        PictureSource()
    }.flow
}