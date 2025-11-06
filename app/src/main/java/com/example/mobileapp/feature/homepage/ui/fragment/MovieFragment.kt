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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobileapp.databinding.FragmentHomeMovieBinding
import com.example.mobileapp.feature.homepage.domain.model.MovieItem
import com.example.mobileapp.feature.homepage.ui.adapter.MovieAdapter
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
                    binding.recyclerViewMovies.visibility = View.VISIBLE
                    Log.d("MovieFragment", "Movie items displayed")
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
        Toast.makeText(requireContext(), "Clicked: ${item.title}", Toast.LENGTH_SHORT).show()
        Log.d("MovieFragment", "Movie item clicked: ${item.title}")
        // TODO: Navigate to movie detail screen
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}