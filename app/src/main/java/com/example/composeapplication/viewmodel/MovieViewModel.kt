package com.example.composeapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.composeapplication.Utils
import com.example.composeapplication.bean.MoviePro
import com.example.composeapplication.bean.ResultData
import com.example.composeapplication.model.RemoteMovieData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MovieViewModel"

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val remoteMovieData = RemoteMovieData.getInstance(application.applicationContext)

    private var movieLiveData = MutableLiveData<ResultData>()
    private var movieProLiveData = MutableLiveData<MoviePro>()

    val movies: LiveData<ResultData> = movieLiveData
    val moviePro: LiveData<MoviePro> = movieProLiveData

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
            Log.d(TAG, "searchMoviesComposeCoroutines: $result")

            movieLiveData.value = result
        }
    }

    suspend fun getMovieComposeCoroutines(id: String) {
        Utils.logDebug(Utils.TAG_SEARCH, "MovieModel getMovieComposeCoroutines with id:$id")
        if (!Utils.ensureNetworkAvailable(getApplication())) return

        val gotMovie = remoteMovieData.getMovieByCoroutines(id)
        Utils.logDebug(Utils.TAG_SEARCH, "MovieModel getMovieComposeCoroutines movie:$gotMovie")

        movieProLiveData.value = gotMovie
    }
}