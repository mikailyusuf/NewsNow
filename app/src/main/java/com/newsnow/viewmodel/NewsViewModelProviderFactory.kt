package com.newsnow.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.newsnow.repository.NewsRepository

class NewsViewModelProviderFactory(val app:Application, val repository: NewsRepository) : ViewModelProvider.Factory
{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app,repository) as T
    }


}