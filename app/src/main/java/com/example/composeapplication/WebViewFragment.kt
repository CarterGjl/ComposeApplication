package com.example.composeapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.screen.ArticleDetailScreen

class WebViewFragment : Fragment() {
    var title:String? = null
    var url:String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString("title")
        url = arguments?.getString("url")
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeApplicationTheme {
                    ArticleDetailScreen(detailUrl = url!!, title!!){
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}