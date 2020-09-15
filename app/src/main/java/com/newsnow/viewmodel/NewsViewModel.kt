package com.newsnow.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.*
import com.newsnow.NewsApplication
import com.newsnow.model.Article
import com.newsnow.model.NewsResponse
import com.newsnow.repository.NewsRepository
import com.newsnow.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application, val repository: NewsRepository) : AndroidViewModel(app) {

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

        safebreakingNewsCall(countryCode)
    }


    fun searchNews(searchQuery: String) = viewModelScope.launch {

        safeSearchCall(searchQuery)

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

    private suspend fun safebreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternet()) {
                val response = repository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
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


    private suspend fun safeSearchCall(Query: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternet()) {
                val response = repository.searchNews(Query, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
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


    fun hasInternet(): Boolean {
        val connectivityManager =
            getApplication<NewsApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilties =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilties.hasTransport(TRANSPORT_WIFI) -> true
                capabilties.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilties.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false

            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> true

                }
            }
        }
        return false
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }

    fun deleteNews(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    fun getSavedNews() = repository.getSavedNews()


}