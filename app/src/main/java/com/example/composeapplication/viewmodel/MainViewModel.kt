package com.example.composeapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


private const val HOME_PAGE_SELECTED_INDEX = "home_page_selected_index"

class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val mSelectLiveData = MutableLiveData<Int>()

    fun getSelectedIndex(): LiveData<Int> {
        if (mSelectLiveData.value == null) {
            val index = savedStateHandle.get<Int>(HOME_PAGE_SELECTED_INDEX) ?: 0
            mSelectLiveData.postValue(index)
        }
        return mSelectLiveData
    }

    fun saveSelectIndex(selectIndex: Int) {
        savedStateHandle.set(HOME_PAGE_SELECTED_INDEX, selectIndex)
        mSelectLiveData.value = selectIndex
    }
}