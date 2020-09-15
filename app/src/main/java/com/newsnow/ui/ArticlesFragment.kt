package com.newsnow.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.newsnow.R
import com.newsnow.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_articles.*


class ArticlesFragment : Fragment(R.layout.fragment_articles) {

    lateinit var newsViewModel: NewsViewModel
    val TAG = "TESTTAG"
    val args : ArticlesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG," ARTICLES FRAGMENT")
        newsViewModel = (activity as NewsActivity).newsViewModel

        val article = args.article
        Log.d(TAG,article.toString())
        Log.d(TAG,article.id.toString())


        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }

        }

        fab.setOnClickListener {
                newsViewModel.saveArticle(article)
            Snackbar.make(view,"News Saved", Snackbar.LENGTH_SHORT).show()
        }
    }
}