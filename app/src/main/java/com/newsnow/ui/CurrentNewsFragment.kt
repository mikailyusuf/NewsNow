package com.newsnow.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newsnow.R
import com.newsnow.adapters.NewsAdapter
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()

        newsAdapter.setOnItemClickListener {

            val bundle = Bundle().apply {
                putSerializable("article", it)

            }

            findNavController().navigate(R.id.action_currentNewsFragment_to_articleActivity, bundle)
        }


//        newsViewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
//
//            when (response) {
//                is Resource.Success -> {
//                    hideProgressBar()
//                    response.data?.let { newsList ->
//
////                        newsAdapter.differ.submitList(newsList.articles.toList())
//                        val totalPages = newsList.totalResults / QUERY_PAGE_SIZE + 2
//                        isLastPage = newsViewModel.breakingNewsPage == totalPages
//                        if (isLastPage) {
//                            recyclerView.setPadding(0, 0, 0, 0)
//                        }
//
//                    }
//                }
//
//                is Resource.Error -> {
//                    hideProgressBar()
//                    response.message?.let {
//                        Toast.makeText(activity, "Error Occured: $it", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                is Resource.Loading -> {
//                    showProgressBar()
//                }
//
//
//            }
//
//        })


        newsAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                showProgressBar()
            } else {
                hideProgressBar()
                // getting the error
                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }

                error.let {
                    if (it != null) {
                        Log.d("error",it.error.message.toString())

//                        Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            newsViewModel.articles.collectLatest { article ->
                newsAdapter.submitData(article)
//                newsAdapter.submitList(article)


            }

        }

        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsList ->

//                        newsAdapter.differ.submitList(newsList.articles.toList())

                        val totalPages = newsList.totalResults / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewModel.searchNewsPage == totalPages
                        if (isLastPage) {
                            recyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        Toast.makeText(activity, "Error Occured: $it", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }


            }

        })

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false

    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true

    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPOsition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.itemCount
            val totalitemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPOsition + visibleItemCount >= totalitemCount
            val isNotatBeginning = firstVisibleItemPOsition >= 0
            val isTotalMoreThanVisible = totalitemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isScrolling && isNotatBeginning && isTotalMoreThanVisible

            if (shouldPaginate) {
                newsViewModel.getBreakingNews("ng")
                isScrolling = false
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }

        }
    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@CurrentNewsFragment.scrollListener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        menu.clear()
//        inflater.inflate(R.menu.search_menu, menu)
//        val searchView = SearchView(activity)
//
//        var job: Job? = null
//
//        menu.findItem(R.id.appSearchBar).apply {
//            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
//            actionView = searchView
//        }
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                searchNews(query)
//                return false
//            }
//
//            override fun onQueryTextChange(query: String): Boolean {
//
//                job?.cancel()
//                job = MainScope().launch {
//                    delay(SEARCH_DELAY)
//                    searchNews(query)
//                }
//
//                return false
//            }
//        })
//
//        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
//            override fun onClose(): Boolean {
//                activity?.let { view?.let { it1 -> Utils.hideSoftKeyBoard(it, it1) } }
//                newsViewModel.getBreakingNews("ng")
//                return false
//            }
//
//        })
//
//        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener {
//            override fun onFocusChange(p0: View?, p1: Boolean) {
//                activity?.let { view?.let { it1 -> Utils.hideSoftKeyBoard(it, it1) } }
//
//            }
//
//        })
//    }

    private fun searchNews(searhQuery: String) {
        newsViewModel.getSearchNews(searhQuery)


    }
}

object Utils {

    fun hideSoftKeyBoard(context: Context, view: View) {
        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }

    }
}