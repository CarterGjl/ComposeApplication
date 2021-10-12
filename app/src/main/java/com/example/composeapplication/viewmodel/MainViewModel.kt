package com.example.composeapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.composeapplication.Event


private const val HOME_PAGE_SELECTED_INDEX = "home_page_selected_index"


sealed class Screen


class WebViewScreen(val title: String, val url: String) : Screen()

class PhotoScreen(val url: String) : Screen()

object CameraScreen : Screen()


class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val mSelectLiveData = MutableLiveData<Int>()

    private val _navigateTo = MutableLiveData<Event<Screen>>()
    val navigateTo: LiveData<Event<Screen>> = _navigateTo

    fun getSelectedIndex(): LiveData<Int> {
        if (mSelectLiveData.value == null) {
            val index = savedStateHandle.get<Int>(HOME_PAGE_SELECTED_INDEX) ?: 0
            mSelectLiveData.postValue(index)
        }
        return mSelectLiveData
    }

    fun navigateToWebViewPage(title: String, url: String) {
        _navigateTo.value = Event(WebViewScreen(title = title, url = url))
    }

    fun navigateToPhotoPage(url: String) {
        _navigateTo.value = Event(PhotoScreen(url = url))
    }

    fun navigateToCameraPage() {
        _navigateTo.value = Event(CameraScreen)
    }

    fun saveSelectIndex(selectIndex: Int) {
        savedStateHandle.set(HOME_PAGE_SELECTED_INDEX, selectIndex)
        mSelectLiveData.value = selectIndex
    }
}