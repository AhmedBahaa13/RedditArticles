package com.bahaa.articlestask.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bahaa.articlestask.domain.RedditResponse

data class RedditResponse(val kind: String, val data: Data){
    fun toRoomResponse() = RedditResponse(kind, data)
}

