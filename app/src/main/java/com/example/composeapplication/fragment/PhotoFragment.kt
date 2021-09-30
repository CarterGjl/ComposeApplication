package com.example.composeapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.R
import com.example.composeapplication.activity.PhotoViewPage
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.screen.MainPage
import com.example.composeapplication.viewmodel.MainViewModel
import com.example.composeapplication.viewmodel.Screen
import com.example.composeapplication.viewmodel.WebViewScreen
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class PhotoFragment : Fragment() {


    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString("url")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                PhotoViewPage(url = url!!) {
                    findNavController().popBackStack()
                }
            }
        }
    }

}