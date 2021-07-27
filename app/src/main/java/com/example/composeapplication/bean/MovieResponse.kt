package com.example.composeapplication.bean

@Suppress("unused")
data class MovieResponse<T>(
    var TotalResults: String = "0",
    var Response: String = "false",
    var Error: String = "null",
    var Search: T
) {
    override fun toString(): String {
        return "MovieSearchResponse(TotalResults=$TotalResults, Response=$Response, Error=$Error, Search=$Search)"
    }
}