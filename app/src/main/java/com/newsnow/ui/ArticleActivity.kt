package com.newsnow.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.newsnow.R
import com.newsnow.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_article.*

@AndroidEntryPoint

class ArticleActivity : AppCompatActivity() {
    private val newsViewModel: NewsViewModel by viewModels()

    private lateinit var webView: WebView

    private val args: ArticleActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        webView = findViewById(R.id.webView)
        val fab = findViewById<FloatingActionButton>(R.id.fab)

        val article = args.article
        val url = article.url


        if (url != null) {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null) {
                        view?.loadUrl(url)
                    }
                    return true
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    progressBar.visibility = View.VISIBLE

                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.INVISIBLE

                }

                override fun onReceivedHttpError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    errorResponse: WebResourceResponse?
                ) {
                    super.onReceivedHttpError(view, request, errorResponse)
                    progressBar.visibility = View.INVISIBLE

                 Toast.makeText(this@ArticleActivity,"Sorry an Error Ocured While loading the Page",Toast.LENGTH_SHORT).show()

                }


            }
//            webView.loadUrl(url)


        }
        fab.setOnClickListener {
            newsViewModel.saveArticle(article)

            Snackbar.make(it, "News Saved", Snackbar.LENGTH_SHORT).show()
            }

        }

    }
