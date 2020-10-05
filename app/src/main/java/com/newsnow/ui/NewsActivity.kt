package com.newsnow.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.newsnow.R
import com.newsnow.db.ArticleDb
import com.newsnow.repository.NewsRepository
import com.newsnow.viewmodel.NewsViewModel
import com.newsnow.viewmodel.NewsViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_news.*


@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val repository = NewsRepository(ArticleDb(this))
        bottom_navigation.setupWithNavController(nav_container.findNavController())
        val viewModelProviderFactory = NewsViewModelProviderFactory(application,repository)
        newsViewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

    }

}