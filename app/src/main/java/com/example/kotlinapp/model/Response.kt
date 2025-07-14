package com.example.kotlinapp.model

data class Response(
    val articles: List<ArticleOr>,
    val status: String,
    val totalResults: Int
)