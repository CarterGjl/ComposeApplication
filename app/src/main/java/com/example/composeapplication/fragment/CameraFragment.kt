package com.example.composeapplication.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import com.example.composeapplication.ui.screen.FeatureThatRequiresCameraPermission
import com.google.accompanist.permissions.ExperimentalPermissionsApi

class CameraFragment : Fragment() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                val current = LocalContext.current
                FeatureThatRequiresCameraPermission(navigateToSettingsScreen = {
                    val intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", current.packageName, null)
                        }
                    current.startActivity(intent)
                })
            }
        }
    }

}