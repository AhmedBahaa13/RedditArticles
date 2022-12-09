package com.bahaa.articlestask.di

import android.content.Context
import com.bahaa.articlestask.data.ArticlesApi
import com.bahaa.articlestask.domain.ArticleDao
import com.bahaa.articlestask.domain.ArticlesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    var mBaseUrl = "https://www.reddit.com/"

    @Provides
    fun provideRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(mBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    @Provides
    fun provideServiceApi(retrofit: Retrofit): ArticlesApi {
        return retrofit.create(ArticlesApi::class.java)
    }

    @Provides
    fun provideDao(articlesDatabase: ArticlesDatabase): ArticleDao {
        return articlesDatabase.dao
    }

    @Provides
    fun provideDataBase(@ApplicationContext context: Context): ArticlesDatabase {
        return ArticlesDatabase.getInstance(context)
    }


}