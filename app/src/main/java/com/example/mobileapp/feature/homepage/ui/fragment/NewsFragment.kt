package com.example.mobileapp.feature.homepage.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.mobileapp.databinding.FragmentHomeNewsBinding
import com.example.mobileapp.feature.homepage.domain.model.NewsItem
import com.example.mobileapp.feature.homepage.ui.adapter.NewsAdapter
import com.example.mobileapp.feature.homepage.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentHomeNewsBinding? = null
    private val binding get() = _binding!!
    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("NewsFragment", "onViewCreated called")

        setupViewPager()
        observeViewModel()
    }

    private fun setupViewPager() {
        newsAdapter = NewsAdapter { newsItem ->
            onNewsItemClick(newsItem)
        }

        binding.viewPagerNews.apply {
            adapter = newsAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            // Get the RecyclerView inside ViewPager2
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            // Add page transformer for carousel effect
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(16))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }

            setPageTransformer(compositePageTransformer)
        }

        Log.d("NewsFragment", "ViewPager setup complete")
    }

    private fun observeViewModel() {
        // Observe news items
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.newsItems.collect { newsItems ->
                Log.d("NewsFragment", "Received ${newsItems.size} news items")

                if (newsItems.isNotEmpty()) {
                    newsAdapter.submitList(newsItems)
                    binding.viewPagerNews.visibility = View.VISIBLE
                    Log.d("NewsFragment", "News items displayed")
                } else {
                    binding.viewPagerNews.visibility = View.GONE
                    Log.d("NewsFragment", "No news items to display")
                }
            }
        }

        // Observe loading state
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                Log.d("NewsFragment", "Loading state: $isLoading")
            }
        }

        // Observe errors
        viewLifecycleOwner.lifecycleScope.launch {
            newsViewModel.isError.collect { error ->
                error?.let {
                    showError(it)
                    Log.e("NewsFragment", "Error: $it")
                }
            }
        }
    }

    private fun onNewsItemClick(item: NewsItem) {
        Toast.makeText(requireContext(), "Clicked: ${item.title}", Toast.LENGTH_SHORT).show()
        Log.d("NewsFragment", "News item clicked: ${item.title}")
        // TODO: Navigate to news detail screen
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
