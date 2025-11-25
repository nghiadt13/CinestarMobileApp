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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.FragmentHomeMovieBinding
import com.example.mobileapp.feature.homepage.domain.model.MovieItem
import com.example.mobileapp.feature.homepage.ui.adapter.MovieAdapter
import com.example.mobileapp.feature.homepage.ui.helper.CarouselEffectHelper
import com.example.mobileapp.feature.homepage.ui.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding: FragmentHomeMovieBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        Log.d("MovieFragment", "onViewCreated called")
        
        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        // Setup filter tabs
        binding.textViewNowShowing.setOnClickListener {
            onTabSelected(0)
        }
        
        binding.textViewSpecial.setOnClickListener {
            onTabSelected(1)
        }
        
        binding.textViewComingSoon.setOnClickListener {
            onTabSelected(2)
        }
        
        Log.d("MovieFragment", "UI setup complete")
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter { movieItem ->
            onMovieItemClick(movieItem)
        }

        binding.recyclerViewMovies.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = movieAdapter
            setHasFixedSize(true)
            clipToPadding = false
            clipChildren = false

            // Add snap helper to snap items to center
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)

            // Add carousel effect
            val carouselEffect = CarouselEffectHelper(
                scaleDownBy = 0.15f,  // Scale down by 15%
                translationYBy = 80f  // Move down by 80dp
            )
            addOnScrollListener(carouselEffect)

            // Add infinite scroll repositioning listener
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    // Only reposition when scroll has stopped
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val layoutMgr = recyclerView.layoutManager as? LinearLayoutManager
                        layoutMgr?.let { lm ->
                            val firstVisiblePosition = lm.findFirstVisibleItemPosition()
                            val realItemCount = movieAdapter.getRealItemCount()

                            if (realItemCount == 0) return

                            val totalItemCount = movieAdapter.itemCount
                            val threshold = realItemCount * 10

                            when {
                                firstVisiblePosition < threshold -> {
                                    // Too close to the start, jump to equivalent position further right
                                    val currentRealPosition = firstVisiblePosition % realItemCount
                                    val newPosition = (totalItemCount / 2) + currentRealPosition
                                    recyclerView.scrollToPosition(newPosition)
                                    Log.d("MovieFragment", "Repositioned from $firstVisiblePosition to $newPosition (near start)")
                                }
                                firstVisiblePosition > totalItemCount - threshold -> {
                                    // Too close to the end, jump to equivalent position further left
                                    val currentRealPosition = firstVisiblePosition % realItemCount
                                    val newPosition = (totalItemCount / 2) + currentRealPosition
                                    recyclerView.scrollToPosition(newPosition)
                                    Log.d("MovieFragment", "Repositioned from $firstVisiblePosition to $newPosition (near end)")
                                }
                            }
                        }
                    }
                }
            })
        }

        Log.d("MovieFragment", "RecyclerView setup complete")
    }

    private fun observeViewModel() {
        // Observe movie items
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.movieItem.collect { movieItems ->
                Log.d("MovieFragment", "Received ${movieItems.size} movie items")

                if (movieItems.isNotEmpty()) {
                    movieAdapter.submitList(movieItems)

                    // Calculate middle position for infinite scrolling
                    val realItemCount = movieAdapter.getRealItemCount()
                    val totalItemCount = movieAdapter.itemCount

                    // Start at a position in the middle of our virtual list
                    val middlePosition = totalItemCount / 2
                    val startPosition = middlePosition - (middlePosition % realItemCount)

                    binding.recyclerViewMovies.post {
                        binding.recyclerViewMovies.scrollToPosition(startPosition)
                        Log.d("MovieFragment", "Set initial position to $startPosition (real item: ${startPosition % realItemCount}) out of $totalItemCount total items")

                        // Trigger carousel effect after layout is complete
                        binding.recyclerViewMovies.viewTreeObserver.addOnGlobalLayoutListener(
                            object : android.view.ViewTreeObserver.OnGlobalLayoutListener {
                                override fun onGlobalLayout() {
                                    // Remove listener to avoid multiple calls
                                    binding.recyclerViewMovies.viewTreeObserver.removeOnGlobalLayoutListener(this)

                                    // Manually trigger carousel effect for initial state
                                    val layoutManager = binding.recyclerViewMovies.layoutManager as? LinearLayoutManager
                                    layoutManager?.let {
                                        for (i in 0 until binding.recyclerViewMovies.childCount) {
                                            val child = binding.recyclerViewMovies.getChildAt(i)
                                            val position = it.getPosition(child)
                                            val centerX = binding.recyclerViewMovies.width / 2f
                                            val childCenterX = (child.left + child.right) / 2f
                                            val distance = Math.abs(centerX - childCenterX)
                                            val maxDistance = binding.recyclerViewMovies.width / 2f
                                            val scale = 1f - (distance / maxDistance) * 0.15f

                                            child.scaleX = scale.coerceAtLeast(0.85f)
                                            child.scaleY = scale.coerceAtLeast(0.85f)
                                            child.translationY = if (scale < 1f) 80f else 0f
                                        }
                                    }

                                    Log.d("MovieFragment", "Initial carousel effect applied")
                                }
                            }
                        )
                    }

                    binding.recyclerViewMovies.visibility = View.VISIBLE
                    Log.d("MovieFragment", "Movie items displayed with infinite scroll")
                } else {
                    binding.recyclerViewMovies.visibility = View.GONE
                    Log.d("MovieFragment", "No movie items to display")
                }
            }
        }

        // Observe loading state
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.isLoading.collect { isLoading ->
                // You can add a progress bar to the layout if needed
                Log.d("MovieFragment", "Loading state: $isLoading")
            }
        }

        // Observe errors  
        viewLifecycleOwner.lifecycleScope.launch {
            movieViewModel.isError.collect { error ->
                error?.let {
                    showError(it)
                    Log.e("MovieFragment", "Error: $it")
                }
            }
        }
    }

    private fun onTabSelected(tab: Int) {
        when (tab) {
            0 -> {
                // Now Showing selected
                updateTabSelection(true, false, false)
                Toast.makeText(requireContext(), "Now Showing selected", Toast.LENGTH_SHORT).show()
                Log.d("MovieFragment", "Now Showing tab selected")
            }
            1 -> {
                // Special selected
                updateTabSelection(false, true, false)
                Toast.makeText(requireContext(), "Special selected", Toast.LENGTH_SHORT).show()
                Log.d("MovieFragment", "Special tab selected")
            }
            2 -> {
                // Coming Soon selected
                updateTabSelection(false, false, true)
                Toast.makeText(requireContext(), "Coming Soon selected", Toast.LENGTH_SHORT).show()
                Log.d("MovieFragment", "Coming Soon tab selected")
            }
        }
    }

    private fun updateTabSelection(nowShowing: Boolean, special: Boolean, comingSoon: Boolean) {
        // Update tab visual states
        binding.textViewNowShowing.isSelected = nowShowing
        binding.textViewSpecial.isSelected = special
        binding.textViewComingSoon.isSelected = comingSoon
        
        // TODO: Update background colors based on selection
        // TODO: Fetch different movie data based on selected tab
    }

    private fun onMovieItemClick(item: MovieItem) {
        Log.d("MovieFragment", "Movie item clicked: ${item.title}, ID: ${item.id}")
        try {
            val action = HomePageFragmentDirections.actionHomePageToMovieDetail(item.id.toLong())
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e("MovieFragment", "Navigation error: ${e.message}")
            Toast.makeText(requireContext(), "Không thể mở chi tiết phim", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}