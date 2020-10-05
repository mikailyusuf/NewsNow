package com.newsnow.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.newsnow.di.NewsApplication
import com.newsnow.model.Article
import com.newsnow.model.NewsResponse
import com.newsnow.repository.NewsRepository
import com.newsnow.utils.Resource
import kotlinx.coroutines.launch
import androidx.hilt.lifecycle.ViewModelInject
import com.newsnow.utils.NetWorkHelper
import retrofit2.Response
import java.io.IOException


class NewsViewModel @ViewModelInject constructor(
    private val repository: NewsRepository,
    private val networkHelper: NetWorkHelper
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null


    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null


    init {

        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {

        getNews(countryCode)
    }


    fun getSearchNews(searchQuery: String) = viewModelScope.launch {

        searchNews(searchQuery)

    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->

                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = result
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = result.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(
                    breakingNewsResponse ?: result
                )
            }
        }

        return Resource.Error(response.message())


    }

    private suspend fun getNews(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = repository.getBreakingNews(countryCode, breakingNewsPage)

                if (response.isSuccessful) {
                    response.body()?.let { result ->

                        breakingNewsPage++
                        if (breakingNewsResponse == null) {
                            breakingNewsResponse = result
                        } else {
                            val oldArticle = breakingNewsResponse?.articles
                            val newArticle = result.articles
                            oldArticle?.addAll(newArticle)
                        }
                        breakingNews.postValue(Resource.Success(
                            breakingNewsResponse ?: result))

                    }
                }
                else{
                    breakingNews.postValue( Resource.Error(response.message()))

                }

            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Error"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }

    }


    private suspend fun searchNews(Query: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (networkHelper.isNetworkConnected()) {
                val response = repository.searchNews(Query, searchNewsPage)

                if (response.isSuccessful)
                {
                    response.body()?.let { result ->

                        searchNewsPage++
                        if (searchNewsResponse == null) {
                            breakingNewsResponse = result
                        } else {
                            val oldArticle = searchNewsResponse?.articles
                            val newArticle = result.articles
                            oldArticle?.addAll(newArticle)
                        }
                        searchNews.postValue(Resource.Success(
                            searchNewsResponse ?: result))

                    }
                }

                else{
                    searchNews.postValue( Resource.Error(response.message()))

                }

            } else {
                searchNews.postValue(Resource.Error("No Internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Error"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }

    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->

                searchNewsPage++
                if (searchNewsResponse == null) {
                    breakingNewsResponse = result
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = result.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(
                    searchNewsResponse ?: result
                )
            }
        }

        return Resource.Error(response.message())


    }



    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }

    fun deleteNews(article: Article) = viewModelScope.launch {

        repository.deleteArticle(article)
    }

    fun getSavedNews() = repository.getArticles()


}