package com.bahaa.articlestask.data

import com.bahaa.articlestask.data.models.RedditResponse
import retrofit2.Response
import retrofit2.http.GET

interface ArticlesApi {
    @GET("r/kotlin/.json")
   suspend fun getArticles(): Response<RedditResponse>
}
