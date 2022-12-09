package com.bahaa.articlestask.presentaion.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bahaa.articlestask.R
import com.bahaa.articlestask.data.models.Article
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import javax.inject.Inject


private const val ITEM = 0
private const val ITEM_WITH_OUT_THUMBNAIL = 1

class ArticlesAdapter @Inject constructor(private val listener: RecyclerItemsClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = ArrayList<Article>()

    override fun getItemViewType(position: Int): Int {
        val length = list[position].thumbnail?.length ?: 0
        val lastThreeChars = list[position].thumbnail?.substring(length.minus(3) ,length)
        val isImageUrl = lastThreeChars == "png" || lastThreeChars == "jpg"
        Log.d("Adapter", "lastThreeChars: $lastThreeChars isImageUrl $isImageUrl")
       return when{
            !list[position].thumbnail.isNullOrEmpty() && isImageUrl -> ITEM

            !list[position].secure_media?.oembed?.thumbnail_url.isNullOrEmpty()  -> ITEM

            else -> ITEM_WITH_OUT_THUMBNAIL
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM) ItemViewHolder.getViewHolder(parent)
        else ItemNoThumbnailViewHolder.getViewHolder(parent)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("Adapter", "onBindViewHolder: ${list[position]}")
        when (holder) {
            is ItemViewHolder -> {
                val article = list[position]
                holder.title.text = article.title
                val imageUrl = if (article.secure_media != null) {
                    article.secure_media?.oembed?.thumbnail_url
                } else {
                    article.thumbnail
                }
                Glide.with(holder.imageView).load(imageUrl).into(holder.imageView)
                holder.itemView.setOnClickListener { view -> listener.itemOnClick(article, view) }
            }
            is ItemNoThumbnailViewHolder -> {
                val article = list[position]
                holder.title.text = article.title
                holder.itemView.setOnClickListener { view -> listener.itemOnClick(article, view) }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var imageView: ShapeableImageView

        init {
            title = itemView.findViewById(R.id.title)
            imageView = itemView.findViewById(R.id.imageView)
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): ItemViewHolder =
                ItemViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.article_item, parent, false)
                )

        }
    }

    class ItemNoThumbnailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView

        init {
            title = itemView.findViewById(R.id.title)
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): ItemNoThumbnailViewHolder =
                ItemNoThumbnailViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.article_item_without_thumbnail, parent, false)
                )

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setArticlesList(list: List<Article>?) {
        if (list != null) {
            this.list = list as ArrayList<Article>
        }
        notifyDataSetChanged()
    }

    interface RecyclerItemsClickListener {
        fun itemOnClick(article: Article?, view: View?)
    }


}