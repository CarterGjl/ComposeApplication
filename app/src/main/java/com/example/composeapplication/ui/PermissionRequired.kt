package com.example.composeapplication.ui

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequired(
    permissionState: PermissionState,
    permissionNotGrantedContent: @Composable (() -> Unit),
    permissionNotAvailableContent: @Composable (() -> Unit),
    content: @Composable (() -> Unit),
) {
    when {
        permissionState.status.isGranted -> {
            content()
        }
        permissionState.status.shouldShowRationale ->{
            permissionNotGrantedContent()
        }
        else -> {
            permissionNotAvailableContent()
        }
    }
}