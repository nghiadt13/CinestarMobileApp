package com.example.mobileapp.feature.payment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentPaymentBinding
import com.example.mobileapp.feature.payment.domain.model.BookingInfo
import com.example.mobileapp.feature.payment.domain.model.PaymentSummary
import com.example.mobileapp.feature.payment.ui.adapter.ComboAdapter
import com.example.mobileapp.feature.payment.ui.adapter.PaymentMethodAdapter
import com.example.mobileapp.feature.payment.ui.viewmodel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentViewModel by viewModels()

    private lateinit var comboAdapter: ComboAdapter
    private lateinit var paymentMethodAdapter: PaymentMethodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupAdapters()
        observeViewModel()
    }

    private fun setupViews() {
        // Back button
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Payment button
        binding.btnPay.setOnClickListener {
            viewModel.processPayment()
        }
    }

    private fun setupAdapters() {
        // Combo adapter
        comboAdapter = ComboAdapter { comboId, change ->
            viewModel.updateComboQuantity(comboId, change)
        }
        binding.rvCombos.adapter = comboAdapter

        // Payment method adapter
        paymentMethodAdapter = PaymentMethodAdapter { method ->
            viewModel.selectPaymentMethod(method)
        }
        binding.rvPaymentMethods.adapter = paymentMethodAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookingInfo.collect { bookingInfo ->
                bookingInfo?.let { updateBookingInfo(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.combos.collect { combos ->
                comboAdapter.submitList(combos)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.paymentMethods.collect { methods ->
                paymentMethodAdapter.submitList(methods)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.paymentSummary.collect { summary ->
                summary?.let { updatePaymentSummary(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isProcessingPayment.collect { isProcessing ->
                updatePaymentButtonState(isProcessing)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.paymentSuccess.collect { isSuccess ->
                if (isSuccess) {
                    showPaymentSuccess()
                    viewModel.resetPaymentSuccess()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }
    }

    private fun updateBookingInfo(info: BookingInfo) {
        binding.apply {
            // Movie poster
            Glide.with(imgMoviePoster.context)
                .load(info.moviePosterUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .centerCrop()
                .into(imgMoviePoster)

            // Movie info
            tvGenreTag.text = info.genre
            tvMovieTitle.text = info.movieTitle
            tvMovieMeta.text = "${info.format} • ${info.duration}"

            // Cinema info
            tvCinemaName.text = info.cinemaName
            tvShowtime.text = info.showtime
            tvShowdate.text = info.showdate
            tvSeats.text = info.seats.joinToString(", ")
            tvRoom.text = info.room
        }
    }

    private fun updatePaymentSummary(summary: PaymentSummary) {
        binding.apply {
            tvTicketPrice.text = viewModel.formatPrice(summary.ticketPrice)
            tvComboPrice.text = viewModel.formatPrice(summary.comboPrice)
            tvDiscount.text = "-${viewModel.formatPrice(summary.discount)}"
            tvTotalPrice.text = viewModel.formatPrice(summary.totalPrice)

            // Update button text
            btnPay.text = "Thanh toán ${viewModel.formatPrice(summary.totalPrice)}"
        }
    }

    private fun updatePaymentButtonState(isProcessing: Boolean) {
        binding.apply {
            btnPay.isEnabled = !isProcessing
            if (isProcessing) {
                btnPay.text = "Đang xử lý..."
                btnPay.alpha = 0.7f
            } else {
                viewModel.paymentSummary.value?.let {
                    btnPay.text = "Thanh toán ${viewModel.formatPrice(it.totalPrice)}"
                }
                btnPay.alpha = 1f
            }
        }
    }

    private fun showPaymentSuccess() {
        Toast.makeText(
            requireContext(),
            "Thanh toán thành công! Vé QR code đã được gửi về máy.",
            Toast.LENGTH_LONG
        ).show()

        // Navigate back or to success screen
        // findNavController().navigate(...)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
