package com.newsnow.model

import com.newsnow.model.Article
import java.io.Serializable

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
): Serializable