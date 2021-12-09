@file:Suppress("unused")

package com.example.composeapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.composeapplication.activity.bsae.BaseActivity
import com.example.composeapplication.ui.ComposeApplicationTheme
import com.example.composeapplication.ui.screen.ArticleDetailScreen
import com.google.accompanist.insets.ProvideWindowInsets

class WebViewActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(ARTICLE_URL)
        val title = intent.getStringExtra(ARTICLE_TITLE)

        setContent {
            ComposeApplicationTheme {
                ProvideWindowInsets(consumeWindowInsets = false) {
                    ArticleDetailScreen(detailUrl = url!!, title!!)
                }
            }
        }
    }

    companion object {
        const val ARTICLE_URL = "article_url"
        const val ARTICLE_TITLE = "article_title"
        fun go(context: Activity, url: String, title: String = "") {
            with(Intent(context, WebViewActivity::class.java)) {
                putExtra(ARTICLE_URL, url)
                putExtra(ARTICLE_TITLE, title)
                context.startActivity(this)
            }
        }
    }

}