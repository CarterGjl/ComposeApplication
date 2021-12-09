@file:Suppress("EXPERIMENTAL_ANNOTATION_ON_OVERRIDE_WARNING")

package com.example.composeapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.navigation.navigate
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.screen.MainPage
import com.example.composeapplication.viewmodel.MainViewModel
import com.example.composeapplication.viewmodel.PictureViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private val pictureViewModel: PictureViewModel by viewModels()

    @ExperimentalCoilApi
    @ExperimentalPagerApi
    @ExperimentalFoundationApi
    @ExperimentalPermissionsApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.navigateTo.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { screen ->
                navigate(screen)
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                ProvideWindowInsets(consumeWindowInsets = false) {
                    ComposeApplicationTheme {
                        MainPage(viewModel)
                    }
                }
            }
        }
    }

}