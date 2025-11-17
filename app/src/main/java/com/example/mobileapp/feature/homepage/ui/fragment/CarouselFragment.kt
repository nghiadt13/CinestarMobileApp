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

            // Add page change callback for infinite scrolling repositioning
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val realItemCount = carouselAdapter.getRealItemCount()
                    if (realItemCount == 0) return

                    val totalItemCount = carouselAdapter.itemCount

                    // If we're getting close to the boundaries, reposition to equivalent position in the middle
                    // This ensures we never run out of items to scroll
                    val threshold = realItemCount * 10 // Keep at least 10 cycles of items on each side

                    when {
                        position < threshold -> {
                            // Too close to the start, jump to equivalent position further right
                            val newPosition = position + (realItemCount * 100)
                            post { setCurrentItem(newPosition, false) }
                            Log.d("CarouselFragment", "Repositioned from $position to $newPosition (near start)")
                        }
                        position > totalItemCount - threshold -> {
                            // Too close to the end, jump to equivalent position further left
                            val newPosition = position - (realItemCount * 100)
                            post { setCurrentItem(newPosition, false) }
                            Log.d("CarouselFragment", "Repositioned from $position to $newPosition (near end)")
                        }
                    }
                }
            })
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

                    // Calculate middle position for infinite scrolling
                    // This ensures we can scroll both left and right from the start
                    val realItemCount = carouselAdapter.getRealItemCount()
                    val totalItemCount = carouselAdapter.itemCount

                    // Start at a position in the middle of our virtual list
                    // that corresponds to the first item (index 0)
                    val middlePosition = totalItemCount / 2
                    val startPosition = middlePosition - (middlePosition % realItemCount)

                    binding.viewPagerBanner.post {
                        binding.viewPagerBanner.setCurrentItem(startPosition, false)
                        Log.d("CarouselFragment", "Set initial position to $startPosition (real item: ${startPosition % realItemCount}) out of $totalItemCount total items")
                    }

                    binding.viewPagerBanner.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                    Log.d("CarouselFragment", "Carousel items displayed with infinite scroll")
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