package com.newsnow.api

import androidx.lifecycle.LiveData
import com.newsnow.model.Article
import com.newsnow.model.NewsResponse
import retrofit2.Response


interface ApiHelper {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<NewsResponse>
    suspend fun searchNews( searchQuery:String, pageNumber: Int): Response<NewsResponse>
    suspend fun insert(article: Article):Long
    fun getArticles(): LiveData<List<Article>>
    suspend fun deleteArticle(article: Article)

}
