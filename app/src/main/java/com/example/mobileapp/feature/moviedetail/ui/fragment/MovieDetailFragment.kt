package com.example.mobileapp.feature.moviedetail.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentMovieDetailBinding
import com.example.mobileapp.feature.moviedetail.ui.adapter.MovieDetailAdapter
import com.example.mobileapp.feature.moviedetail.ui.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    private var adapter: MovieDetailAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        adapter = MovieDetailAdapter(requireContext(), binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        adapter?.setupWebView()
        adapter?.setupCommentsRecyclerView()
        observeViewModel()

        // Fetch movie detail with ID from navigation args
        viewModel.fetchMovieDetail(args.movieId.toLong())
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        binding.btnRetry.setOnClickListener { viewModel.fetchMovieDetail(args.movieId.toLong()) }

        binding.buttonBooking.setOnClickListener {
            val action = MovieDetailFragmentDirections.actionMovieDetailToBookingCinema(args.movieId.toLong())
            findNavController().navigate(action)
        }
    }
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieDetail.collect { movieDetail ->
                movieDetail?.let { adapter?.bindMovieDetail(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.scrollView.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.errorView.visibility = View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                if (error != null) {
                    adapter?.showError(error)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Destroy WebView to prevent memory leaks
        adapter?.destroyWebView()
        adapter = null
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        // Pause WebView when fragment is paused
        adapter?.pauseWebView()
    }

    override fun onResume() {
        super.onResume()
        // Resume WebView when fragment is resumed
        adapter?.resumeWebView()
    }
}
