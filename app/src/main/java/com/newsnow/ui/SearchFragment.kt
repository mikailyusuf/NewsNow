package com.newsnow.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newsnow.R
import com.newsnow.adapters.NewsAdapter
import com.newsnow.utils.Constants
import com.newsnow.utils.Constants.Companion.SEARCH_DELAY
import com.newsnow.utils.Resource
import com.newsnow.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_current_news.*
import kotlinx.android.synthetic.main.fragment_current_news.progressBar
import kotlinx.android.synthetic.main.fragment_current_news.recyclerView
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

//    lateinit var newsViewModel: NewsViewModel
    private val newsViewModel : NewsViewModel by viewModels()

    lateinit var newsAdapter: NewsAdapter
    val TAG = "SearchFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        newsViewModel = (activity as NewsActivity).newsViewModel
        var job:Job? = null
        setupRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(R.id.action_searchFragment_to_articlesFragment,bundle)
        }

        searchQuery.addTextChangedListener {editable->

            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_DELAY)
                editable.let {
                    if (editable.toString().isNotEmpty())
                    {
                        newsViewModel.getSearchNews(editable.toString())
                    }
                }
            }

        }


        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer {response->

            when(response)
            {
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let {newsList->

                        newsAdapter.differ.submitList(newsList.articles.toList())

                        val totalPages = newsList.totalResults / Constants.QUERY_PAGE_SIZE +2
                        isLastPage  = newsViewModel.searchNewsPage == totalPages
                        if (isLastPage)
                        {
                            recyclerView.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error->
                {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity,"Error Occured: $it", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading ->
                {
                    showProgressBar()
                }



            }

        })
    }
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPOsition  = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.itemCount
            val totalitemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPOsition + visibleItemCount >= totalitemCount
            val isNotatBeginning = firstVisibleItemPOsition >= 0
            val isTotalMoreThanVisible = totalitemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isScrolling && isNotatBeginning && isTotalMoreThanVisible

            if (shouldPaginate){
                newsViewModel.getSearchNews(searchQuery.text.toString())
                isScrolling = false
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            {
                isScrolling = true
            }

        }
    }

    private fun hideProgressBar()
    {
        progressBar.visibility = View.INVISIBLE
        isLoading = false

    }

    private fun showProgressBar()
    {
        progressBar.visibility = View.VISIBLE
        isLoading = true

    }


    private fun setupRecycler()
    {
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            addOnScrollListener(this@SearchFragment.scrollListener)
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
