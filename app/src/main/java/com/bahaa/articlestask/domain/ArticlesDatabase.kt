package com.bahaa.articlestask.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bahaa.articlestask.domain.RedditResponse

@Database(entities = [RedditResponse::class], version = 4)
@TypeConverters(ArticleTypeConverter::class)
abstract class ArticlesDatabase:RoomDatabase() {
   abstract val dao:ArticleDao

   companion object{
       @Volatile
       private var instance:ArticlesDatabase? = null

       fun getInstance(context: Context):ArticlesDatabase{
           return instance?: synchronized(this){
               Room.databaseBuilder(context,ArticlesDatabase::class.java,"articles_data_base").fallbackToDestructiveMigration().build()
           }
       }
   }
}