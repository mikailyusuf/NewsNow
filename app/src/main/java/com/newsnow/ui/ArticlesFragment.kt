package com.newsnow.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.newsnow.R
import com.newsnow.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_articles.*


class ArticlesFragment : Fragment() {

    lateinit var newsViewModel: NewsViewModel

    val args : ArticlesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as NewsActivity).newsViewModel

        val article = args.article


        webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }

        }
    }
}