package com.bahaa.articlestask.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bahaa.articlestask.data.models.Data

@Entity(tableName = "ArticlesResponse")
data class RedditResponse(val kind: String?, val data: Data?, @PrimaryKey var id:Int?=null)
