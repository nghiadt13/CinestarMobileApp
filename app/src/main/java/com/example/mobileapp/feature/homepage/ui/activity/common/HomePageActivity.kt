package com.example.mobileapp.feature.homepage.ui.activity.common

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileapp.databinding.ActivityHomepageBinding
import com.example.mobileapp.feature.homepage.ui.adapter.HomeItem
import com.example.mobileapp.feature.homepage.ui.adapter.HomePageAdapter
import com.example.mobileapp.feature.homepage.ui.viewmodel.CarouselViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomepageBinding
    private val carouselViewModel: CarouselViewModel by viewModels()
    private lateinit var homePageAdapter: HomePageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("HomePageActivity", "onCreate called")
        
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        homePageAdapter = HomePageAdapter { carouselItem ->
            Toast.makeText(this, "Clicked: ${carouselItem.title}", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomePageActivity)
            adapter = homePageAdapter
            setHasFixedSize(true)
        }
        
        Log.d("HomePageActivity", "RecyclerView setup complete")
    }

    private fun observeViewModel() {
        // Observe loading state
        lifecycleScope.launch {
            carouselViewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                Log.d("HomePageActivity", "Loading state: $isLoading")
            }
        }
        
        // Observe carousel items
        lifecycleScope.launch {
            carouselViewModel.carouselItem.collect { carouselItems ->
                Log.d("HomePageActivity", "Received ${carouselItems.size} carousel items")
                
                if (carouselItems.isNotEmpty()) {
                    val homeItems = listOf(HomeItem.CarouselSection(carouselItems))
                    homePageAdapter.submitList(homeItems)
                    Log.d("HomePageActivity", "Submitted carousel section with ${carouselItems.size} items")
                } else {
                    Log.d("HomePageActivity", "No carousel items to display")
                }
            }
        }
        
        // Observe errors
        lifecycleScope.launch {
            carouselViewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(this@HomePageActivity, "Error: $it", Toast.LENGTH_LONG).show()
                    Log.e("HomePageActivity", "Error: $it")
                }
            }
        }
    }
}
