package com.example.composeapplication.viewmodel

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.composeapplication.bean.Article
import com.example.composeapplication.model.RemoteSevice

private const val TAG = "ArticleSource"
class ArticleSource : PagingSource<Int, Article>() {
    override fun getRefreshKey(state: PagingState<Int, Article>): Int {
        Log.d(TAG, "getRefreshKey: ")
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val instance = RemoteSevice.getInstance()
            val nextPage = params.key ?: 0
            val data = instance.getArticles(nextPage)
            if (data.data.size < 15) {
                return LoadResult.Error(Exception("no more result"))
            }
            Log.d(TAG, "load: data size ${data.data.size} next page $nextPage")
            LoadResult.Page(
                data = data.data.datas,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "load: ${e.message}", e)
            LoadResult.Error(e)
        }
    }
}