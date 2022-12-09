package com.bahaa.articlestask.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class Data(@SerializedName("children") val childrenList: List<Children>)

data class Children(@SerializedName("data") val article: Article)

@Parcelize
data class Article(
    @SerializedName("url_overridden_by_dest")
    var thumbnail: String? = null,
    @SerializedName("selftext")
    var body: String? = null,
    var secure_media: SecureMedia? = null,
    var url: String? = null,
    var title: String? = null
) : Parcelable
@Parcelize
data class SecureMedia(val oembed: Oembed?) : Parcelable

@Parcelize
data class Oembed(val height: Int?, val width: Int?, val thumbnail_url: String?) : Parcelable