package com.newsnow.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.newsnow.R
import com.newsnow.adapters.NewsAdapter
import com.newsnow.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_current_news.*
import kotlinx.android.synthetic.main.fragment_current_news.recyclerView
import kotlinx.android.synthetic.main.fragment_saved_news.*

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

        var itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                newsViewModel.deleteNews(article)
                Snackbar.make(view, "Successfully DEleted News", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        newsViewModel.saveArticle(article)
                    }.show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(recyclerView)
        }

        newsViewModel.getSavedNews().observe(viewLifecycleOwner, Observer { article->
            newsAdapter.differ.submitList(article)
        })
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