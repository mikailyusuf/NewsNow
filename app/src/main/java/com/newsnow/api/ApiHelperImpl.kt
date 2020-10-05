package com.newsnow.api

import androidx.lifecycle.LiveData
import com.newsnow.db.ArticleDao
import com.newsnow.db.ArticleDb
import com.newsnow.model.Article
import com.newsnow.model.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val api: NewsApi, val db: ArticleDao) : ApiHelper {
    override suspend fun getBreakingNews(
        countryCode: String,
        pageNumber: Int
    ): Response<NewsResponse> =
        api.getBreakingNews(countryCode, pageNumber)


    override suspend fun searchNews(searchQuery: String, pageNumber: Int): Response<NewsResponse> =
        api.searchNews(searchQuery)


    override suspend fun insert(article: Article): Long =
        db.inset(article)


    override fun getArticles(): LiveData<List<Article>> =
        db.getArticles()


    override suspend fun deleteArticle(article: Article) =
        db.deleteArticle(article)

}