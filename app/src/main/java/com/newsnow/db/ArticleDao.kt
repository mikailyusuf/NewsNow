package com.newsnow.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.newsnow.model.Article


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inset(article: Article):Long

    @Query("SELECT * FROM articles")
    fun getArticles():LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}