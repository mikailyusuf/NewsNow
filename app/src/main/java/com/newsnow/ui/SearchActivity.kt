package com.newsnow.ui

import android.app.Activity
import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsnow.R
import com.newsnow.adapters.NewsAdapter2
import com.newsnow.utils.Resource
import com.newsnow.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search.*

@AndroidEntryPoint
class SearchActivity : Activity() {
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        handleIntent(intent)

        setupRecycler()

        newsViewModel.searchNews.observe(this, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsList ->

                        newsAdapter.differ.submitList(newsList.articles.toList())

                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(this, "Error Occured: $it", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }


            }

        })

    }


    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            //use the query to search your data somehow
            searchNews(query.toString())

        }
    }

    private fun searchNews(searhQuery: String) {
        newsViewModel.getSearchNews(searhQuery)

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE

    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter2()
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }
}