package com.example.composeapplication.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.composeapplication.R
import com.example.composeapplication.viewmodel.PhotoScreen
import com.example.composeapplication.viewmodel.Screen
import com.example.composeapplication.viewmodel.WebViewScreen

fun Fragment.navigate(to: Screen) {

    when (to) {
        is PhotoScreen -> {
            val bundle = Bundle()
            bundle.putString("url", to.url)
            findNavController().navigate(R.id.photoFragment, bundle)
        }
        is WebViewScreen -> {
            val bundle = Bundle()
            bundle.putString("title", to.title)
            bundle.putString("url", to.url)
            findNavController().navigate(R.id.webViewFragment2, bundle)
        }
    }
}