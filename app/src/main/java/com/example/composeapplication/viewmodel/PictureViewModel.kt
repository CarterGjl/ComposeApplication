package com.example.composeapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.paging.compose.LazyPagingItems
import com.example.composeapplication.bean.PictureModel
import com.example.composeapplication.model.Http
import com.example.composeapplication.viewmodel.picture.PictureSource
import kotlinx.coroutines.flow.Flow

private const val TAG = "PictureViewModel"

class PictureViewModel :BaseViewModel() {

    var laLazyPagingItems : LazyPagingItems<PictureModel>? =null

    fun setLazyPagingItems(laLazyPagingItems: LazyPagingItems<PictureModel>) {
        this.laLazyPagingItems = laLazyPagingItems
    }

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