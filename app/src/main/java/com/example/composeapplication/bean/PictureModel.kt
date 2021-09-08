package com.example.composeapplication.bean

class PictureModel(val url: String)

class NewsModelModel(
    val top_stories: List<TopStoryModel> = listOf(),
    val stories: MutableList<StoryModel> = mutableListOf()
)

data class Girls(
    val data: List<DataGirl>,
    val page: Int,
    val page_count: Int,
    val status: Int,
    val total_counts: Int
)

data class DataGirl(
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