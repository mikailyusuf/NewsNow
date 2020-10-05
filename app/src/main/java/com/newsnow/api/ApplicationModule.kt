package com.newsnow.api

import android.content.Context
import androidx.room.Room
import com.newsnow.db.ArticleDb
import com.newsnow.utils.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {


    @Provides
    @Singleton
    fun provideArtcleDB(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        ArticleDb::class.java,
        "article_db.db"
    ).build()


    @Provides
    @Singleton
    fun provideArticleDAO(articleDb: ArticleDb) = articleDb.getArticleDao()

    @Provides
    @Singleton
    fun provideRetrofit(): NewsApi {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(NewsApi::class.java)
    }

}