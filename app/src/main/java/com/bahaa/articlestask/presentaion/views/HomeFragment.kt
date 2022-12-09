package com.bahaa.articlestask.presentaion.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bahaa.articlestask.databinding.FragmentHomeBinding
import com.bahaa.articlestask.presentaion.adapters.ArticlesAdapter
import com.bahaa.articlestask.presentaion.viewmodel.ArticlesViewModel
import com.bahaa.articlestask.utils.Constants
import com.bahaa.articlestask.R
import com.bahaa.articlestask.data.Resource
import com.bahaa.articlestask.data.models.Article

import dagger.hilt.android.AndroidEntryPoint


@Suppress("UNCHECKED_CAST")
@AndroidEntryPoint
class HomeFragment : Fragment(), ArticlesAdapter.RecyclerItemsClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ArticlesViewModel
    private var adapter: ArticlesAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        viewModel = ViewModelProvider(this)[ArticlesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.title = getString(R.string.home_title)
        setUpRecyclerView()
        getData()
        binding.tryGetData.setOnClickListener {
            getData()
        }
    }

    private fun getData(){
        viewModel.getNews()
        lifecycleScope.launchWhenStarted {
            viewModel.newsFlow.collect { resultResource: Resource? ->
                when (resultResource) {
                    is Resource.Success<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.noDataAvailable.visibility = View.GONE
                        binding.tryGetData.visibility = View.GONE
                        adapter!!.setArticlesList((resultResource as Resource.Success<List<Article>>).result)
                    }
                    is Resource.Error<*> -> {
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.GONE
                        binding.noDataAvailable.visibility = View.VISIBLE
                        binding.tryGetData.visibility = View.VISIBLE
                        Toast.makeText(
                            activity,
                            "Error : " + (resultResource as Resource.Error<String>).error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.noDataAvailable.visibility = View.GONE
                        binding.tryGetData.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        adapter = ArticlesAdapter(this)
        binding.recyclerView.adapter = adapter
    }

    override fun itemOnClick(article: Article?, view: View?) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.ARTICLE, article)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_homeFragment_to_articleFragment, bundle)
    }
}