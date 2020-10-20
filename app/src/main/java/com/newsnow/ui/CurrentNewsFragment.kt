package com.newsnow.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newsnow.R
import com.newsnow.adapters.NewsAdapter
import com.newsnow.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.newsnow.utils.Resource
import com.newsnow.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_current_news.*


@AndroidEntryPoint
class CurrentNewsFragment : Fragment(R.layout.fragment_current_news) {
    private val newsViewModel : NewsViewModel by viewModels()

//    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG = "CurrentNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        newsViewModel = (activity as NewsActivity).newsViewModel
        setupRecycler()
        newsAdapter.setOnItemClickListener {

          val url = it.url
//            val bundle = ("url" to url)
//            val bundle = Bundle().apply {
//                putSerializable("article",it)
//
//            }
            val intent = Intent(activity,ArticleActivity::class.java)
            intent.putExtra("url",url)
            startActivity(intent)


//            findNavController().navigate(R.id.action_currentNewsFragment_to_articleActivity,bundle)
        }


        newsViewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->

            when(response)
            {
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let {newsList->

                        newsAdapter.differ.submitList(newsList.articles.toList())
                        val totalPages = newsList.totalResults / QUERY_PAGE_SIZE +2
                        isLastPage  = newsViewModel.breakingNewsPage == totalPages
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
                       Toast.makeText(activity,"Error Occured: $it",Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading ->
                {
                    showProgressBar()
                }



            }

        })

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
            val isTotalMoreThanVisible = totalitemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isScrolling && isNotatBeginning && isTotalMoreThanVisible

            if (shouldPaginate){
                newsViewModel.getBreakingNews("ng")
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


    private fun setupRecycler()
    {
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@CurrentNewsFragment.scrollListener)
        }
    }

}