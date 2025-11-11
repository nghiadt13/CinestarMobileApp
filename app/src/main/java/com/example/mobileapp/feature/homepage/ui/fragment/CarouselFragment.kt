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
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.mobileapp.databinding.FragmentHomeCarouselBinding
import com.example.mobileapp.feature.homepage.domain.model.CarouselItem
import com.example.mobileapp.feature.homepage.ui.adapter.CarouselAdapter
import com.example.mobileapp.feature.homepage.ui.viewmodel.CarouselViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CarouselFragment : Fragment() {

    private var _binding: FragmentHomeCarouselBinding? = null
    private val binding get() = _binding!!
    private val carouselViewModel: CarouselViewModel by viewModels()
    private lateinit var carouselAdapter: CarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCarouselBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CarouselFragment", "onViewCreated called")
        
        setupViewPager()
        observeViewModel()
    }

    private fun setupViewPager() {
        carouselAdapter = CarouselAdapter { carouselItem ->
            onCarouselItemClick(carouselItem)
        }

        binding.viewPagerBanner.apply {
            adapter = carouselAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            
            // Configure inner RecyclerView
            getChildAt(0)?.let { child ->
                if (child is androidx.recyclerview.widget.RecyclerView) {
                    child.overScrollMode = View.OVER_SCROLL_ALWAYS
                    child.clipToPadding = false
                    child.clipChildren = false
                }
            }

            // Set page transformers
            val transformer = CompositePageTransformer()
            transformer.addTransformer(MarginPageTransformer(30))
            setPageTransformer(transformer)
        }
        
        Log.d("CarouselFragment", "ViewPager2 setup complete")
    }

    private fun observeViewModel() {
        // Observe carousel items
        viewLifecycleOwner.lifecycleScope.launch {
            carouselViewModel.carouselItem.collect { carouselItems ->
                Log.d("CarouselFragment", "Received ${carouselItems.size} carousel items")

                if (carouselItems.isNotEmpty()) {
                    carouselAdapter.submitList(carouselItems)

                    // Set initial position to middle for infinite scrolling
                    // This allows scrolling both left and right
                    val middlePosition = carouselAdapter.itemCount / 2
                    val startPosition = middlePosition - (middlePosition % carouselAdapter.getRealItemCount())

                    binding.viewPagerBanner.post {
                        binding.viewPagerBanner.setCurrentItem(startPosition, false)
                    }

                    binding.viewPagerBanner.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                    Log.d("CarouselFragment", "Carousel items displayed with infinite scroll at position $startPosition")
                } else {
                    binding.viewPagerBanner.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                    Log.d("CarouselFragment", "No carousel items to display")
                }
            }
        }

        // Observe loading state
        viewLifecycleOwner.lifecycleScope.launch {
            carouselViewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                Log.d("CarouselFragment", "Loading state: $isLoading")

            }
        }

        // Observe errors
        viewLifecycleOwner.lifecycleScope.launch {
            carouselViewModel.error.collect { error ->
                error?.let {
                    showError(it)
                    Log.e("CarouselFragment", "Error: $it")
                }
            }
        }
    }

    private fun onCarouselItemClick(item: CarouselItem) {
        Toast.makeText(requireContext(), "Clicked: ${item.title}", Toast.LENGTH_SHORT).show()
        Log.d("CarouselFragment", "Carousel item clicked: ${item.title}")
        // TODO: Navigate to detail screen
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}