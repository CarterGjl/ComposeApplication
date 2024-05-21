package com.example.composeapplication

import android.Manifest
import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.bluetooth.BluetoothA2dp
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.AccessAlarms
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.core.view.postDelayed
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.example.composeapplication.activity.bsae.BaseActivity
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.screen.MainPage
import com.example.composeapplication.ui.screen.SplashAdScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi


// 官方demo地址
// https://github.com/android/compose-samples
val LocalSnackbarHostState =
    compositionLocalOf<SnackbarHostState> { error("LocalSnackbarHostState 没有提供值！") }
private const val TAG = "MainActivity"
class MainActivity : BaseActivity(), SplashScreen.OnExitAnimationListener {

    private val receive = BluetoothStateBroadcastReceive()
    private var requestPermissionName: String = ""

    private val requestSinglePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (key in it.keys) {
                val granted = it[key]
                if (granted == true && key == Manifest.permission.BLUETOOTH_CONNECT) {
                    //同意授权
                    val bluetoothManager: BluetoothManager =
                        getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

                    val adapter = bluetoothManager.adapter

                    adapter
                        .getProfileProxy(this, object : BluetoothProfile.ServiceListener {
                            override fun onServiceConnected(
                                profile: Int,
                                proxy: BluetoothProfile?
                            ) {
                                val devices: List<BluetoothDevice>? = proxy?.connectedDevices
                                Log.d(TAG, "onServiceConnected: devices $devices")
                            }

                            override fun onServiceDisconnected(profile: Int) {
                                Log.d(TAG, "onServiceDisconnected: ")
                            }

                        }, BluetoothProfile.HEADSET)
                    Log.d("-,-,-", "$requestPermissionName granted")
                } else {
                    Log.d("-,-,-", "$requestPermissionName not granted")
                    //未同意授权
                    if (!shouldShowRequestPermissionRationale(requestPermissionName)) {
                        //用户拒绝权限并且系统不再弹出请求权限的弹窗
                        //这时需要我们自己处理，比如自定义弹窗告知用户为何必须要申请这个权限
                        Log.e(
                            "-,-,-",
                            "$requestPermissionName not granted and should not show rationale"
                        )
                    }
                }
            }

        }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @OptIn(
        ExperimentalPermissionsApi::class,
        ExperimentalFoundationApi::class,
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permission: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH
            )
        }

        requestSinglePermissionLauncher.launch(permission)

//        val audioManager: AudioManager = getSystemService(AudioManager::class.java)
//        val listener =
//            OnCommunicationDeviceChangedListener { device -> // Handle changes
//
//                Log.d(TAG, "onCreate: $device")
//            }
//        audioManager.addOnCommunicationDeviceChangedListener(mainExecutor, listener)
//
//        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
//        //            try {
////                field = PowerManager.class.getField("PROXIMITY_SCREEN_OFF_WAKE_LOCK").getInt(null);
////            } catch (Throwable t) {
////                VoiceChannel.voipError(TAG, "", t);
////            }
//        val wakeLockLevelSupported =
//            powerManager.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)
//
//        Log.d(TAG, "onCreate: wakeLockLevelSupported  $wakeLockLevelSupported")
//        val wakeLockProximity = powerManager.newWakeLock(
//            PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "Baiduhi" +
//                    ":CallProximitySensor"
//        )
//        wakeLockProximity.setReferenceCounted(false)
//        wakeLockProximity.acquire(2 * 3600 * 1000)
//        Utils.ensureNetworkAvailable(application)

        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        intentFilter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF")
        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(
                receive, intentFilter,
                RECEIVER_NOT_EXPORTED
            )
        } else {
            registerReceiver(receive, intentFilter)
        }
        val splashScreen = installSplashScreen()
        setContent {

            ComposeApplicationTheme {
                val rememberScaffoldState = remember { SnackbarHostState() }
                CompositionLocalProvider(LocalSnackbarHostState provides rememberScaffoldState) {
                    MainPage()
                }
            }
        }
        splashScreen.setKeepOnScreenCondition {
            false
        }

        splashScreen.setOnExitAnimationListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receive)
    }



    override fun onSplashScreenExit(splashScreenViewProvider: SplashScreenViewProvider) {

        if (splashScreenViewProvider.view is ViewGroup) {
            //显示一个广告或者启动页推广,自己实践玩耍吧,建议把mainViewModel.mockDataLoading()延时降低，然后测试
            val composeView = ComposeView(this@MainActivity).apply {
                setContent {
                    SplashAdScreen(onCloseAd = {
                        splashScreenViewProvider.remove()
                    })
                }
            }
            (splashScreenViewProvider.view as ViewGroup).addView(composeView)
            return
        }

        val flag = true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || flag) {
            // 使用alpha透明度动画过渡
            val splashScreenView = splashScreenViewProvider.view
            val endAlpha = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) 0F else -2F
            val alphaObjectAnimator =
                ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1F, endAlpha)
            alphaObjectAnimator.duration = 500L
            alphaObjectAnimator.interpolator = FastOutLinearInInterpolator()
            alphaObjectAnimator.doOnEnd {
                splashScreenViewProvider.remove()
            }
            alphaObjectAnimator.start()
            return
        }

        //下面是所有使用动态背景的，我们让中心图标做一个动画然后离开
        val splashScreenView = splashScreenViewProvider.view
        val iconView = splashScreenViewProvider.iconView
        val isCompatVersion = Build.VERSION.SDK_INT < Build.VERSION_CODES.R
        val slideUp = ObjectAnimator.ofFloat(
            iconView,
            View.TRANSLATION_Y,
            0f,
            -splashScreenView.height.toFloat()
        )
        slideUp.interpolator = AnticipateInterpolator()
        slideUp.duration = if (isCompatVersion) 1000L else 200L
        slideUp.doOnEnd {
            splashScreenViewProvider.remove()
        }
        if (isCompatVersion) {
            //低版本的系统，我们让图标做完动画再关闭
            waitForAnimatedIconToFinish(splashScreenViewProvider, splashScreenView, slideUp)
        } else {
            slideUp.start()
        }
    }

    private fun waitForAnimatedIconToFinish(
        splashScreenViewProvider: SplashScreenViewProvider,
        view: View,
        animator: Animator
    ) {
        val delayMillis: Long = (
                splashScreenViewProvider.iconAnimationStartMillis +
                        splashScreenViewProvider.iconAnimationDurationMillis
                ) - System.currentTimeMillis()
        view.postDelayed(delayMillis) { animator.start() }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun BoxScope() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)

            .blur(30.dp)
            .background(Color.DarkGray)
//            .graphicsLayer(renderEffect = BlurEffect(radiusX = 25F, radiusY = 25F))
    ) {

    }
    Text(text = "empty")
}


const val MINE = "mine"
const val NEST = "nest"
const val DIALOG = "dialog"

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {

    data object Article : Screen("article", R.string.article, Icons.AutoMirrored.Filled.Article)
    data object Picture : Screen("picture", R.string.picture, Icons.Filled.Favorite)
    data object Weather : Screen("weather", R.string.weather, Icons.Filled.AccountBox)
    data object Test : Screen("test", R.string.test, Icons.Filled.AccessAlarms)
    data object Mine : Screen("mine", R.string.mine, Icons.Filled.AdminPanelSettings)
    data object Music : Screen("music", R.string.music, Icons.AutoMirrored.Filled.QueueMusic)
    data object ArticleDetail :
        Screen("article_detail?url={url}", R.string.detail, Icons.Filled.AccountBox)

    data object Search : Screen("search", R.string.search, Icons.Filled.Search)
    data object WebView : Screen("webview", R.string.search, Icons.Filled.Search)
    data object TypeTree : Screen("type", R.string.knowledge, Icons.Filled.Search)
}


class BluetoothStateBroadcastReceive : BroadcastReceiver() {

    @Suppress("DEPRECATION")
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java
            )
        } else {
            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        Log.d(TAG, "onReceive: $action")
        when (action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> Toast.makeText(
                context,
                "蓝牙设备:" + device!!.getName() + "已链接",
                Toast.LENGTH_SHORT
            ).show()

            BluetoothDevice.ACTION_ACL_DISCONNECTED -> Toast.makeText(
                context,
                "蓝牙设备:" + device!!.getName() + "已断开",
                Toast.LENGTH_SHORT
            ).show()

            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                when (blueState) {
                    BluetoothAdapter.STATE_OFF -> Toast.makeText(
                        context,
                        "蓝牙已关闭",
                        Toast.LENGTH_SHORT
                    ).show()

                    BluetoothAdapter.STATE_ON -> Toast.makeText(
                        context,
                        "蓝牙已开启",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}