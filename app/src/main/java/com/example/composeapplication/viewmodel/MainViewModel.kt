@file:Suppress("unused")

package com.example.composeapplication.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.*
import androidx.navigation.NavHostController
import com.example.composeapplication.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private const val HOME_PAGE_SELECTED_INDEX = "home_page_selected_index"


sealed class Screen


class WebViewScreen(val title: String, val url: String) : Screen()

class PhotoScreen(val url: String) : Screen()

data object CameraScreen : Screen()


class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private var navController:NavHostController? = null

    private val mSelectLiveData = MutableLiveData<Int>()

    private val _navigateTo = MutableLiveData<Event<Screen>>()
    private val id = MutableLiveData<Int>()

    val titleId: LiveData<Int> = id

    val navigateTo: LiveData<Event<Screen>> = _navigateTo

    fun getSelectedIndex(): LiveData<Int> {
        if (mSelectLiveData.value == null) {
            val index = savedStateHandle.get<Int>(HOME_PAGE_SELECTED_INDEX) ?: 0
            mSelectLiveData.postValue(index)
        }
        return mSelectLiveData
    }

    var dataLoaded: Boolean = false

    fun mockDataLoading(): Boolean {
        viewModelScope.launch {
            delay(1000)
            dataLoaded = true
        }
        return dataLoaded
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

    fun showTitle(@StringRes id:Int){
        this.id.value = id
    }

    fun setNavControllerA(navController: NavHostController) {
        this.navController = navController
    }

    fun navigateTo(string: String) {
        navController?.navigate(string)
    }

    fun popUp() {
        navController?.popBackStack()
    }
}