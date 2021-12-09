package com.example.composeapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import androidx.paging.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.composeapplication.Utils
import com.example.composeapplication.bean.Article
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.model.RemoteSevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MovieViewModel"

@Suppress("unused")
class ArticleViewModel(application: Application) : AndroidViewModel(application) {
    private val remoteMovieData = RemoteSevice.getInstance()

    private var articalLiveData = MutableLiveData<ResultData>()

    init {
        Log.d(TAG, ": ArticleViewModel init")
    }

    val articlesFrom: LiveData<ResultData> = articalLiveData

    fun searchMoviesComposeCoroutines() {
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

    private val articles: Flow<PagingData<Article>> = Pager(PagingConfig(pageSize = 15)) {
        ArticleSource()
    }.flow.cachedIn(viewModelScope)


    var viewStates by mutableStateOf(ArticlesState(pagingData = articles))
        private set


    data class ArticlesState(
        val isRefreshing: Boolean = false,
        val listState: LazyListState = LazyListState(),
        val pagingData: PagingArticle
    )
}

typealias PagingArticle = Flow<PagingData<Article>>