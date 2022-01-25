package com.example.composeapplication.viewmodel.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapplication.bean.HotKey
import com.example.composeapplication.bean.HotKeyResult
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.model.RemoteSevice
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"
class SearchViewModel : ViewModel() {

    private val _searchResult = MutableLiveData<ResultData>()
    var searchResult = _searchResult

    private val _hotKeyResult = MutableLiveData<List<HotKey>>()
    var hotKeyResult = _hotKeyResult

    fun searchArticle(searchKey: String) {
        viewModelScope.launch {
            val async = viewModelScope.async {
                RemoteSevice.getInstance().searchArticle(key = searchKey)
            }
            val result = async.await()
            Log.d(TAG, "searchArticle: $result")
            _searchResult.value = result
        }
    }

    fun getHotKeys(){
        viewModelScope.launch {
            val async = viewModelScope.async {
                RemoteSevice.getInstance().getHotKeys()
            }
            val result = async.await()
            Log.d(TAG, "searchArticle: $result")
            _hotKeyResult.value = result.data
        }
    }
}