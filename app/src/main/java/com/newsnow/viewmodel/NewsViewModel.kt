package com.newsnow.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.newsnow.model.NewsResponse
import com.newsnow.repository.NewsRepository
import com.newsnow.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val repository: NewsRepository):ViewModel() {

    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var  breakingNewsPage = 1
    var breakingNewsResponse:NewsResponse ? = null


    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var  searchNewsPage = 1
    var searchNewsResponse:NewsResponse ? = null




    init {

        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode:String) = viewModelScope.launch {

        breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(countryCode,breakingNewsPage)
        handleBreakingNewsResponse(response)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }


    fun searchNews(searchQuery:String) = viewModelScope.launch {

        searchNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(searchQuery,breakingNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))

    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>
    {
     if (response.isSuccessful)
     {
         response.body()?.let {result->

             breakingNewsPage ++
             if(breakingNewsResponse  == null)
             {
                 breakingNewsResponse = result
             }
             else{
                 val oldArticle = breakingNewsResponse?.articles
                 val newArticle = result.articles
                oldArticle?.addAll(newArticle)
             }
             return  Resource.Success(breakingNewsResponse?:result
             )
         }
     }

        return Resource.Error(response.message())


    }



    private fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>
    {
        if (response.isSuccessful)
        {
            response.body()?.let {result->

                searchNewsPage ++
                if(searchNewsResponse  == null)
                {
                    breakingNewsResponse = result
                }
                else{
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = result.articles
                    oldArticle?.addAll(newArticle)
                }
                return  Resource.Success(searchNewsResponse?:result
                )
            }
        }

        return Resource.Error(response.message())


    }



}