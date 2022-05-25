package com.example.composeapplication.activity.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.Companion.ACTION_REQUEST_PERMISSIONS
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions.Companion.EXTRA_PERMISSIONS
import androidx.activity.result.registerForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun ActivityResultCaller.registerForPermissionResult(
    onGranted: (() -> Unit)? = null,
    onDenied: (() -> Unit)? = null,
    onShowRequestRationale: (() -> Unit)? = null,
): ActivityResultLauncher<String> {

    return registerForActivityResult(RequestPermissionContract()) { result ->
        val permission = result.first
        when {
            // 已授权
            result.second -> onGranted?.let { it -> it() }
            // 提示授权
            permission.isNotEmpty() && ActivityCompat.shouldShowRequestPermissionRationale(
                this as Activity,
                permission
            ) -> onShowRequestRationale?.let { it -> it() }
            // 拒绝授权
            else -> onDenied?.let { it -> it() }
        }
    }
}

// 注：Output输出类型变成了Pair<String, Boolean>
class RequestPermissionContract : ActivityResultContract<String, Pair<String, Boolean>>() {
    // 存储权限的变量
    private lateinit var mPermission: String

    override fun createIntent(context: Context, input: String): Intent {
        // 创建Intent前赋值
        mPermission = input
        return Intent(ACTION_REQUEST_PERMISSIONS).putExtra(EXTRA_PERMISSIONS, arrayOf(input))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<String, Boolean> {
        if (intent == null || resultCode != Activity.RESULT_OK) return mPermission to false
        val grantResults =
            intent.getIntArrayExtra(ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSION_GRANT_RESULTS)
        return mPermission to
                if (grantResults == null || grantResults.isEmpty()) false
                else grantResults[0] == PackageManager.PERMISSION_GRANTED
    }

    override fun getSynchronousResult(
        context: Context,
        input: String
    ): SynchronousResult<Pair<String, Boolean>>? =
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, input) -> {
                SynchronousResult(input to true)
            }
            else -> null
        }
}
