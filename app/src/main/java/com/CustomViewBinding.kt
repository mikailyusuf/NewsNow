package com

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsnow.utils.Resource
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_layout.view.*

@BindingAdapter(value = ["setImageUrl"])
fun ImageView.bindImageUrl(url: String?) {
    if (url != null && url.isNotBlank()) {

//        Glide.with(this).load(url).into(this)


        Picasso.get()
            .load(url)
            .into(this)
    }
}

@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
    this.run {
        this.setHasFixedSize(true)
        this.adapter = adapter
    }
}

//@BindingAdapter(value = ["setupVisibility"])
//fun ProgressBar.progressVisibility(loadingState: Resource?) {
//    loadingState?.let {
//        isVisible = when(it.status) {
//            LoadingState.Status.RUNNING -> true
//            LoadingState.Status.SUCCESS -> false
//            LoadingState.Status.FAILED -> false
//        }
//    }
//}