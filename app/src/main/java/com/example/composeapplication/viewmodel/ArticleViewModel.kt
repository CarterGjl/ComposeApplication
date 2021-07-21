package com.example.composeapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.composeapplication.Utils
import com.example.composeapplication.bean.Article
import com.example.composeapplication.bean.MoviePro
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.model.RemoteSevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MovieViewModel"

class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    private val remoteMovieData = RemoteSevice.getInstance()

    private var articalLiveData = MutableLiveData<ResultData>()

    val articles: LiveData<ResultData> = articalLiveData

    fun searchMoviesComposeCoroutines(keyWorld: String) {
        viewModelScope.launch {

        }

    }
    fun getArticles(page: Int) {
        viewModelScope.launch {
            if (!Utils.ensureNetworkAvailable(getApplication())) return@launch
            val result = withContext(Dispatchers.IO) {
                remoteMovieData.getArticles(page)
            }
            Log.d(TAG, "getArticles: $result")

            articalLiveData.value = result
        }
    }

    val articles1: Flow<PagingData<Article>> = Pager(
        PagingConfig(
            pageSize = 15
        )
    ) {
        ArticleSource()
    }.flow
}