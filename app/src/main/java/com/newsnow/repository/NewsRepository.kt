package com.newsnow.repository

import androidx.lifecycle.LiveData
import com.newsnow.api.ApiHelper
import com.newsnow.api.NewsApi
import com.newsnow.api.RetrofitInstance
import com.newsnow.db.ArticleDb
import com.newsnow.model.Article
import com.newsnow.model.NewsResponse
import com.newsnow.utils.Constants.Companion.API_KEY
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) = apiHelper.getBreakingNews(countryCode,pageNumber = 1)
    suspend fun searchNews( searchQuery:String, pageNumber: Int)=apiHelper.searchNews(searchQuery,pageNumber)
    suspend fun insert(article: Article)=apiHelper.insert(article)
    fun getArticles()=apiHelper.getArticles()
    suspend fun deleteArticle(article: Article) = apiHelper.deleteArticle(article)

}