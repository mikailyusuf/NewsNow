package com.newsnow.repository

import com.newsnow.api.NewsApi
import com.newsnow.api.RetrofitInstance
import com.newsnow.db.ArticleDb
import com.newsnow.utils.Constants.Companion.API_KEY

class NewsRepository(val db: ArticleDb) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNuews(countryCode, pageNumber)

    suspend fun searchNews( searchQuery:String, pageNumber: Int) =
            RetrofitInstance.api.searchNews(searchQuery,pageNumber)

}