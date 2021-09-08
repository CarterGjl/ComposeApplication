package com.example.composeapplication.viewmodel.picture

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.composeapplication.bean.PictureModel
import com.example.composeapplication.model.Http

private const val TAG = "PictureSource"
class PictureSource : PagingSource<Int, PictureModel>() {
    override fun getRefreshKey(state: PagingState<Int, PictureModel>): Int {
        Log.d(TAG, "getRefreshKey: ")
        return state.pages.size - 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PictureModel> {
        return try {
            val instance = Http.getInstance()
            val nextPage = params.key ?: 0
            val data = instance.gank.getPics(nextPage)
            if (data.data.size < 15) {
                return LoadResult.Error(Exception("no more result"))
            }
            Log.d(TAG, "load: data size ${data.data.size} next page $nextPage")
            LoadResult.Page(
                data = data.data,
                prevKey = if (nextPage == 0) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "load: ${e.message}", e)
            LoadResult.Error(e)
        }
    }
}