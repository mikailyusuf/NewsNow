package com.newsnow.repository

import androidx.paging.PagingSource
import com.newsnow.api.ApiHelper
import com.newsnow.model.Article
import com.newsnow.repository.anonymous.PAGE_NUMBER
import com.newsnow.utils.Constants.Companion.NEWS_SOURCE
import javax.inject.Inject

class NewsRepository @Inject constructor(private val apiHelper: ApiHelper) :
    PagingSource<Int, Article>() {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        apiHelper.getBreakingNews(countryCode, pageNumber = 1)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        apiHelper.searchNews(searchQuery, pageNumber)

    suspend fun insert(article: Article) = apiHelper.insert(article)
    fun getArticles() = apiHelper.getArticles()
    suspend fun deleteArticle(article: Article) = apiHelper.deleteArticle(article)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        try {
            val nextPage = params.key ?: 1
            val response = apiHelper.getBreakingNews(NEWS_SOURCE, nextPage)
            return LoadResult.Page(
                data = response.body()!!.articles,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = PAGE_NUMBER + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}

object anonymous {
    var PAGE_NUMBER = 1
}