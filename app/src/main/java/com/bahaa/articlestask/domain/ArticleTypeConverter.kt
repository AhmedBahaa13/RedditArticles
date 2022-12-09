package com.bahaa.articlestask.domain

import androidx.room.TypeConverter
import com.bahaa.articlestask.data.models.Data
import com.bahaa.articlestask.data.models.Oembed
import com.bahaa.articlestask.data.models.SecureMedia
import com.google.gson.Gson

class ArticleTypeConverter {


    @TypeConverter
    fun fromOembedToGson(data: Data):String{
        return Gson().toJson(data)
    }

    @TypeConverter
    fun fromGsonToOembed(string: String):Data{
        return Gson().fromJson(string,Data::class.java)
    }
}