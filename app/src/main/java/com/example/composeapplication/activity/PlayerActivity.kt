package com.example.composeapplication.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.composeapplication.activity.bsae.BaseActivity
import com.example.composeapplication.ui.screen.PlayerPage

class PlayerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PlayerPage()
        }
    }



}