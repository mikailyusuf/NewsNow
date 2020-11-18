package com.newsnow.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newsnow.R
import com.newsnow.adapters.NewsAdapter
import com.newsnow.databinding.FragmentCurrentNewsBinding
import com.newsnow.utils.Constants
import com.newsnow.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.newsnow.utils.Constants.Companion.SEARCH_DELAY
import com.newsnow.utils.Resource
import com.newsnow.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_current_news.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class CurrentNewsFragment : Fragment(R.layout.fragment_current_news) {
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            findNavController().navigate(R.id.action_currentNewsFragment_to_articleActivity, bundle)
        }


        CoroutineScope(Dispatchers.Main).launch {
            newsViewModel.articles.collectLatest { article ->
                newsAdapter.submitData(article)


            }

        }


    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}

