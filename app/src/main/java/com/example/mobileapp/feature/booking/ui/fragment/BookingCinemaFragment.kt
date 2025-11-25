package com.example.mobileapp.feature.booking.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mobileapp.databinding.FragmentBookingCinemaBinding
import com.example.mobileapp.feature.booking.ui.adapter.CinemaAdapter
import com.example.mobileapp.feature.booking.ui.viewmodel.CinemaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingCinemaFragment : Fragment() {

    private var _binding: FragmentBookingCinemaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CinemaViewModel by viewModels()
    private val args: BookingCinemaFragmentArgs by navArgs()
    private lateinit var adapter: CinemaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingCinemaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()

        // Fetch cinemas for the selected movie
        viewModel.fetchCinemasByMovieId(args.movieId)
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter = CinemaAdapter { cinema ->
            val action = BookingCinemaFragmentDirections.actionBookingCinemaToBookingTicket(cinemaName = cinema.name)
            findNavController().navigate(action)
        }
        binding.rvCinemas.adapter = adapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cinemas.collect { cinemas ->
                adapter.submitList(cinemas)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                // TODO: Show/hide loading indicator if needed
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
