package com.example.mobileapp.feature.booking.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentBookingCinemaBinding
import com.example.mobileapp.feature.booking.domain.model.Cinema
import com.example.mobileapp.feature.booking.ui.adapter.CinemaAdapter
import com.example.mobileapp.feature.booking.ui.viewmodel.CinemaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingCinemaFragment : Fragment() {

    private var _binding: FragmentBookingCinemaBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: CinemaViewModel by viewModels()
    private val args: BookingCinemaFragmentArgs by navArgs()

    private lateinit var cinemaAdapter: CinemaAdapter
    private var selectedCinema: Cinema? = null

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
        setupViews()
        setupCinemaList()
        observeViewModel()

        // Fetch cinemas for the selected movie
        viewModel.fetchCinemasByMovieId(args.movieId)
    }

    private fun setupViews() {
        // Back button
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // City selector
        binding.tvCitySelector.setOnClickListener { toggleCityDropdown() }

        binding.tvCityHCM.setOnClickListener { selectCity("TP.HCM") }

        binding.tvCityHN.setOnClickListener { selectCity("Hà Nội") }

        // Search
        binding.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {}
                override fun afterTextChanged(s: Editable?) {
                    viewModel.setSearchQuery(s?.toString() ?: "")
                }
            }
        )

        // Continue button
        binding.btnContinue.setOnClickListener {
            selectedCinema?.let { cinema ->
                val action = BookingCinemaFragmentDirections
                    .actionBookingCinemaToBookingTicket(
                        movieId = args.movieId,
                        cinemaId = cinema.id,
                        cinemaName = cinema.name
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun setupCinemaList() {
        cinemaAdapter = CinemaAdapter { cinema -> onCinemaSelected(cinema) }
        binding.rvCinemas.adapter = cinemaAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredCinemas.collect { cinemas ->
                cinemaAdapter.submitList(cinemas)
                updateEmptyState(cinemas.isEmpty())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding.rvCinemas.visibility = View.GONE
                    binding.tvEmptyState.text = "Đang tải..."
                    binding.tvEmptyState.visibility = View.VISIBLE
                } else {
                    // When loading completes, update UI based on data
                    updateEmptyState(viewModel.filteredCinemas.value.isEmpty())
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    binding.tvEmptyState.text = it
                    binding.tvEmptyState.visibility = View.VISIBLE
                    binding.rvCinemas.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentCity.collect { city ->
                binding.tvCitySelector.text = city
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty && viewModel.error.value == null && !viewModel.isLoading.value) {
            binding.tvEmptyState.text = "Không tìm thấy rạp phim"
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvCinemas.visibility = View.GONE
        } else if (!isEmpty) {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvCinemas.visibility = View.VISIBLE
        }
    }

    private fun selectCity(city: String) {
        viewModel.setCity(city)
        toggleCityDropdown()

        // Clear selection when changing city
        selectedCinema = null
        cinemaAdapter.clearSelection()
        updateContinueButton(false)
    }

    private fun toggleCityDropdown() {
        val isVisible = binding.cityDropdownPopup.visibility == View.VISIBLE

        if (isVisible) {
            // Hide dropdown
            val fadeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_fade_out)
            binding.cityDropdownPopup.startAnimation(fadeOut)
            binding.cityDropdownPopup.visibility = View.GONE
        } else {
            // Show dropdown
            binding.cityDropdownPopup.visibility = View.VISIBLE
            val fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.scale_fade_in)
            binding.cityDropdownPopup.startAnimation(fadeIn)
        }
    }

    private fun onCinemaSelected(cinema: Cinema) {
        selectedCinema = cinema
        updateContinueButton(true)
    }

    private fun updateContinueButton(enabled: Boolean) {
        binding.btnContinue.isEnabled = enabled
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
