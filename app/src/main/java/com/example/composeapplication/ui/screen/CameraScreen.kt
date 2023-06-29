package com.example.composeapplication.ui.screen

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.composeapplication.ui.PermissionRequired
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalPermissionsApi
@Composable
fun FeatureThatRequiresCameraPermission(
    navigateToSettingsScreen: () -> Unit,
    content: @Composable (() -> Unit),
) {
    PermissionRequest(
        navigateToSettingsScreen = navigateToSettingsScreen,
        content = content
    )
}

@ExperimentalPermissionsApi
@Composable
fun PermissionRequest(
    permission: String = Manifest.permission.CAMERA,
    navigateToSettingsScreen: () -> Unit,

    permissionNotAvailable: String = "Camera permission denied. See this FAQ with information about why we need this permission. Please, grant us access on the Settings screen.",
    desc: String = "The camera is important for this app. Please grant the permission.",
    content: @Composable () -> Unit,
) {
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }
    val cameraPermissionState = rememberPermissionState(permission)
    LaunchedEffect(key1 = "permission") {
        cameraPermissionState.launchPermissionRequest()
    }
    PermissionRequired(
        permissionState = cameraPermissionState,
        content = content,
        permissionNotGrantedContent = {
            if (doNotShowRationale) {
                Text("Feature not available")
            } else {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(10.dp)
                ) {
                    Text(desc)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                            Text("Ok!")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { doNotShowRationale = true }) {
                            Text("Nope")
                        }
                    }
                }
            }
        },
        permissionNotAvailableContent = {
            Column {
                Text(
                    ""
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = navigateToSettingsScreen) {
                    Text("Open Settings")
                }
            }
        })
}

@Composable
fun CameraPreview() {

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    val previewView = remember {
        PreviewView(context)
    }
    val cameraProvider = cameraProviderFuture.get()
    val preview = Preview.Builder().build().also {
        it.setSurfaceProvider(previewView.surfaceProvider)
    }
    var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize(),
        ) {
            cameraProviderFuture.addListener(
                {
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                    } catch (_: Exception) {

                    }
                }, ContextCompat.getMainExecutor(context)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .statusBarsPadding()
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                modifier = Modifier.size(30.dp),
                onClick = {
                    cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    }
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
                }) {
                Icon(Icons.Outlined.Cameraswitch, "Camera", tint = Color.White)
            }
        }
    }

}


