package com.example.composeapplication.bean

class PageModel<T>(
    val page: Int,
    val page_count: Int,
    val status: Int,
    val total_counts: Int,
    val data: T
)