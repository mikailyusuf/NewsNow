package com.newsnow.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.newsnow.R
import kotlinx.android.synthetic.main.activity_article.*
//import kotlinx.android.synthetic.main.activity_article.webView
import kotlinx.android.synthetic.main.fragment_articles.*

class ArticleActivity : AppCompatActivity() {

//    private val args:ArticleActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)

////        val article = args.article
//        webView.apply {
//            webViewClient = WebViewClient()
//         loadUrl("https://www.nytimes.com/2020/10/07/arts/music/eddie-van-halen-metallica-kirk-hammett.html")
//        }
    }
}