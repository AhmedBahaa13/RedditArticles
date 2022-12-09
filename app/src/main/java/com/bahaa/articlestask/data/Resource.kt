package com.bahaa.articlestask.data

sealed class Resource {
    object Loading : Resource()
    class Success<T>(val result: T) : Resource()
    class Error<T>(val error: T) : Resource()
}
