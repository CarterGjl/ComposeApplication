package com.example.composeapplication

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarms
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import coil.annotation.ExperimentalCoilApi
import com.example.composeapplication.activity.bsae.BaseActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


// 官方demo地址
// https://github.com/android/compose-samples
private const val TAG = "MainActivity"

class MainActivity : BaseActivity() {

    @ExperimentalCoilApi
    @ExperimentalPagerApi
    @ExperimentalFoundationApi
    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val view = TextView(this)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            view.setRenderEffect(RenderEffect.createBlurEffect(2F, 3F, Shader.TileMode.CLAMP))
//        }
//        val defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
//        RingtoneManager.getRingtone(this,defaultUri).play()
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }
}

val a = test()
fun test() {
//    GlobalScope.launch {
//        flow {
//            emit(1)
//            throw ArithmeticException("div 0")
//        }.catch {
//            Log.d(TAG, "onCreate:catch error $it")
//            println("catch error $it")
//        }.onCompletion {
//            Log.d(TAG, "onCreate finally")
//            println("finally")
//        }.collect {
//            Log.d(TAG, "onCreate collect $it")
//        }
//    }
}



@Preview(device = Devices.PIXEL_2_XL, showBackground = true, showSystemUi = true)
@Composable
fun BoxScope() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(text = "empty")
    }
}
fun  main() {
    val scope = MainScope()
//    scope.launch {
//        val token = withContext(Dispatchers.IO) {
//            getToken()
//        }
//        val profile = withContext(Dispatchers.IO) {
////            loadProfile(token)
//            login("2")
//        }
//    }
}
suspend fun login(await: String): String {
    return withContext(Dispatchers.IO) {
        // 延迟一秒登录
        delay(1000)
        "login"
    }
}

const val MINE = "mine"
const val NEST = "nest"
const val DIALOG = "dialog"

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {

    object Article : Screen("article", R.string.article, Icons.Filled.Article)
    object Picture : Screen("picture", R.string.picture, Icons.Filled.Favorite)
    object Weather : Screen("weather", R.string.weather, Icons.Filled.AccountBox)
    object Test : Screen("test", R.string.test, Icons.Filled.AccessAlarms)
    object ArticleDetail :
        Screen("article_detail?url={url}", R.string.detail, Icons.Filled.AccountBox)
}
