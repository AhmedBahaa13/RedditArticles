package com.bahaa.articlestask.domain.repository

import com.bahaa.articlestask.data.ArticlesApi
import com.bahaa.articlestask.data.Resource
import com.bahaa.articlestask.data.models.Article
import com.bahaa.articlestask.data.models.Children
import com.bahaa.articlestask.data.models.RedditResponse
import com.bahaa.articlestask.domain.ArticleDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response
import javax.inject.Inject

class ArticlesRepository @Inject constructor(private val articleDao: ArticleDao, private val articlesApi: ArticlesApi) {
    private val _dataFlow = MutableStateFlow<Resource>(Resource.Loading)
    val dataFlow : StateFlow<Resource>
        get() = _dataFlow

       suspend fun fetchData(hasInternetConnection: Boolean) {
            if (hasInternetConnection) {
                val response = articlesApi.getArticles()
                if (response.isSuccessful) {
                    handleSuccessResponse(response)
                } else {
                    handleFailureResponse(response)
                }
            } else {
                articleDao.getArticles().collect{
                    val articles = getArticleList(it.data?.childrenList)
                    if (articles.isNotEmpty()) _dataFlow.emit(Resource.Success(articles))
                    else _dataFlow.emit(Resource.Error("There is no Data"))
                }
            }
        }

        private suspend fun handleSuccessResponse(response: Response<RedditResponse>) {
            val articles = getArticleList(response.body()?.data?.childrenList)
            _dataFlow.emit(Resource.Success(articles))
            articleDao.addArticles(response.body()!!.toRoomResponse())
        }

        private suspend fun handleFailureResponse(response: Response<RedditResponse>) {
            articleDao.getArticles().collect {
                val articles = getArticleList(it.data?.childrenList)
                if (articles.isEmpty()) _dataFlow.emit(Resource.Error(response.message()))
                else _dataFlow.emit(Resource.Success(articles))
            }
        }

        private fun getArticleList(childrenList: List<Children>?): List<Article> {
            val articles = ArrayList<Article>()
            childrenList?.forEach {
                articles.add(it.article)
            }
            return articles
        }

}