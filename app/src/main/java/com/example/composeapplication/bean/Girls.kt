package com.example.composeapplication.bean

data class BannerResult(
    val data: List<Banner>,
    val errorCode: Int,
    val errorMsg: String
)

data class Banner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)


class TopStoryModel(
    val id: Int,
    val hint: String,
    val url: String,
    val title: String,
    val image: String
)

class StoryModel(
    val id: Int = 0,
    val hint: String = "",
    val url: String = "",
    val title: String = "",
    val images: List<String> = listOf()
)