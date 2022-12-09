package com.bahaa.articlestask.presentaion.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bahaa.articlestask.R
import com.bahaa.articlestask.data.models.Article
import com.bahaa.articlestask.databinding.FragmentArticleBinding
import com.bahaa.articlestask.utils.Constants
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "ArticlesFragment"

@AndroidEntryPoint
class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private var article: Article? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        article = requireArguments().getParcelable(Constants.ARTICLE) as Article?
        (requireActivity() as AppCompatActivity).supportActionBar?.title = article?.title
        if (article?.body.isNullOrEmpty()) {
            binding.articleBody.visibility = View.GONE
            binding.noDataAvailable.visibility = View.VISIBLE
        } else
            binding.articleBody.text = article?.body ?: ""

        loadImage(article)
        Log.d(TAG, "onViewCreated: Article From Args" + article.toString())
    }

    private fun loadImage(article: Article?) {
        val length = article?.thumbnail?.length ?: 0
        val lastThreeChars = article?.thumbnail?.substring(length.minus(3), length)
        val imageTypes = resources.getStringArray(R.array.Image_extensions)
        val isImageUrl = lastThreeChars == imageTypes[0] || lastThreeChars == imageTypes[1]
        when {
            !article?.thumbnail.isNullOrEmpty() && isImageUrl -> {
                Glide.with(binding.imageView).load(article?.thumbnail).into(
                    binding.imageView
                )
            }

            !article?.secure_media?.oembed?.thumbnail_url.isNullOrEmpty() -> {
                Glide.with(binding.imageView).load(article?.secure_media?.oembed?.thumbnail_url)
                    .into(
                        binding.imageView
                    )
                binding.articleBody.text = article?.url
                binding.articleBody.visibility = View.VISIBLE
                binding.noDataAvailable.visibility = View.GONE
            }

            else -> {
                (binding.imageView.parent as ViewGroup).removeView(binding.imageView)
                binding.container.setPadding(0,10,0,0)
            }
        }
    }
}