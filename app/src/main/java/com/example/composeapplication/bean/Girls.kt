package com.example.composeapplication.bean

data class GirlsResult(
    val data: List<Data>,
    val page: Int,
    val page_count: Int,
    val status: Int,
    val total_counts: Int
)

data class Girl(
    val _id: String,
    val author: String,
    val category: String,
    val createdAt: String,
    val desc: String,
    val images: List<String>,
    val likeCounts: Int,
    val publishedAt: String,
    val stars: Int,
    val title: String,
    val type: String,
    val url: String,
    val views: Int
)

data class BannerResult(
    val data: List<Banner>,
    val status: Int
)

data class Banner(
    val image: String,
    val title: String,
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