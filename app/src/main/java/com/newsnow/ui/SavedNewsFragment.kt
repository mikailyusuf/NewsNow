package com.newsnow.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsnow.R
import com.newsnow.adapters.NewsAdapter
import com.newsnow.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_current_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

lateinit var newsViewModel:NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        newsViewModel = (activity as NewsActivity).newsViewModel

        setupRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(R.id.action_searchFragment_to_articlesFragment,bundle)
        }
    }

    private fun setupRecycler()
    {
        newsAdapter = NewsAdapter()
        recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}