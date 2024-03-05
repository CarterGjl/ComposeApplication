package com.example.composeapplication.viewmodel.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapplication.AppRuntime
import com.example.composeapplication.bean.Article
import com.example.composeapplication.bean.HotKey
import com.example.composeapplication.model.RemoteSevice
import com.example.composeapplication.ui.base.PageState
import com.example.composeapplication.ui.base.PageStateData
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "SearchViewModel"

class SearchViewModel : ViewModel() {

    private val _searchResult = MutableLiveData<List<Article>>()
    var searchResult = _searchResult
    private val pageStateData: MutableLiveData<PageStateData> =
        MutableLiveData(PageStateData(PageState.EMPTY))
    private val _hotKeyResult = MutableLiveData<List<HotKey>>()
    var hotKeyResult = _hotKeyResult

    fun searchArticle(searchKey: String) {
        viewModelScope.launch {
            val async = viewModelScope.async {
                RemoteSevice.getInstance().searchArticle(key = searchKey)
            }
            val result = async.await()
            Log.d(TAG, "searchArticle: $result")
            if (result.data.datas.isEmpty()) {
                AppRuntime.rememberScaffoldState?.showSnackbar("未搜索到关键词相关文章")
                pageStateData.value = PageStateData(PageState.EMPTY)
                return@launch
            } else {
                pageStateData.value = PageStateData(PageState.CONTENT)
            }
            _searchResult.value = result.data.datas
        }
    }

    fun getHotKeys() {
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