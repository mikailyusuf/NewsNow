package com.newsnow.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.newsnow.R
import com.newsnow.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_article.*
//import kotlinx.android.synthetic.main.activity_article.webView
import kotlinx.android.synthetic.main.fragment_articles.*

@AndroidEntryPoint

class ArticleActivity : AppCompatActivity() {
    private val newsViewModel : NewsViewModel by viewModels()

    private lateinit var webView: WebView

//    private val args:ArticleActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        webView = findViewById(R.id.webView)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        val data = intent
        val url = data.getStringExtra("url")

        if (url != null) {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) {
                        view?.loadUrl(url)
                    }
                    return true
                }
            }
            webView.loadUrl(url)
        }
        fab.setOnClickListener {
//            newsViewModel.saveArticle()
        }


    }
}