package com.bahaa.articlestask.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bahaa.articlestask.domain.RedditResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun addArticles(redditResponse: RedditResponse)

    @Query("select * from ArticlesResponse")
   suspend fun getArticles(): Flow<RedditResponse>
}