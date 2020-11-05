package com.newsnow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsnow.R
import com.newsnow.model.Article
import kotlinx.android.synthetic.main.article_layout.view.*
import java.util.*

class NewsAdapter: PagingDataAdapter<Article,NewsAdapter.NewsViewHolder>(ArticleComparator) {


    inner class  NewsViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)


//    private val differCallback =  object :DiffUtil.ItemCallback<Article>(){
//        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return oldItem.url == newItem.url
//        }
//
//        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
//            return  oldItem == newItem
//
//        }
//    }

    object ArticleComparator : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

//    val differ = AsyncListDiffer(this@NewsAdapter,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.article_layout,parent,false))
    }

//    override fun getItemCount(): Int {
//        return  differ.currentList.size
//
//    }

    override fun onBindViewHolder(holder:NewsViewHolder, position: Int) {



        holder.itemView.apply {

//            Glide.with(this).load(article.urlToImage).into(image)
//            source.text = article.source?.name
//            tittle.text = article.title

            tittle.text = getItem(position)?.title
            source.text = getItem(position)?.source?.name
            Glide.with(this).load(getItem(position)?.urlToImage).into(image)
            description.text = getItem(position)?.description
            date.text = getItem(position)?.publishedAt



//            description.text = article.description
//            date.text = article.publishedAt
//            setOnClickListener{
//                onItemClickListener?.let {it(article)
//
//                }
//            }

        }
    }

    private var onItemClickListener:((Article)-> Unit)?= null
    fun setOnItemClickListener(listener:(Article) -> Unit){
        onItemClickListener  = listener
    }

}