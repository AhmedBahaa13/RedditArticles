package com.bahaa.articlestask.presentaion.viewmodel

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bahaa.articlestask.MyApplication
import com.bahaa.articlestask.data.Resource
import com.bahaa.articlestask.domain.repository.ArticlesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(application: Application, private val repository: ArticlesRepository) :
    AndroidViewModel(application) {

    private val _newFlow = MutableStateFlow<Resource>(Resource.Loading)
    val newsFlow :StateFlow<Resource>
    get() = _newFlow


    fun getNews() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchData(hasInternetConnection())
            _newFlow.value = repository.dataFlow.value
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<MyApplication>().getSystemService(ConnectivityManager::class.java)
        val activeNetwork = connectivityManager.activeNetwork ?:return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?:return false
        return when{
            networkCapabilities.hasTransport(TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            networkCapabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}