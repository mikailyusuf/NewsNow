package com.newsnow.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.newsnow.R
import com.newsnow.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_articles.*


class ArticlesFragment : Fragment(R.layout.fragment_articles) {
    private val newsViewModel: NewsViewModel by viewModels()

    private val args: ArticlesFragmentArgs by navArgs()
//    private lateinit var contentView: View

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val contentView = inflater.inflate(R.layout.article_layout, container, false)
//           webView.webViewClient = WebViewClient()

//        webView.webViewClient = WebViewClient()
//        article.url?.let { webView.loadUrl(it) }

        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      val article = args.article
        article.url?.let { Log.d("webview", it) }
//        webView.apply {
//            webViewClient = WebViewClient()
//            article.url?.let { loadUrl(it) }
//        }

//        val article = args.article
//        val webView = view.findViewById<WebView>(R.id.webView)
//
//        webView.webViewClient = WebViewClient()
//        article.url?.let { webView.loadUrl(it) }

//        webView.apply {
//            webViewClient = WebViewClient()
//            article.url?.let { loadUrl(it) }
//
//        }

        fab.setOnClickListener {
//                newsViewModel.saveArticle(article)
            Snackbar.make(view, "News Saved", Snackbar.LENGTH_SHORT).show()
        }
    }
}