package com.example.mobileapp.feature.ticket.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mobileapp.databinding.FragmentBookingTicketBinding
import com.example.mobileapp.feature.ticket.ui.adapter.DateAdapter
import com.example.mobileapp.feature.ticket.ui.adapter.SeatAdapter
import com.example.mobileapp.feature.ticket.ui.adapter.TimeAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class BookingTicketFragment : Fragment() {

    private var _binding: FragmentBookingTicketBinding? = null
    private val binding
        get() = _binding!!

    private val args: BookingTicketFragmentArgs by navArgs()

    private lateinit var dateAdapter: DateAdapter
    private lateinit var timeAdapter: TimeAdapter
    private lateinit var seatAdapter: SeatAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        args.cinemaName?.let { binding.tvCinemaName.text = it }

        setupDates()
        setupTimes()
        setupSeats()

        binding.btnBook.setOnClickListener { showBookingConfirmation() }
    }

    private fun setupDates() {
        dateAdapter = DateAdapter { date ->
            val dateFormat = SimpleDateFormat("EEEE, dd/MM/yyyy", Locale.forLanguageTag("vi-VN"))
            binding.tvSelectedDate.text = dateFormat.format(date).uppercase()
        }
        binding.rvDates.adapter = dateAdapter
    }

    private fun setupTimes() {
        timeAdapter = TimeAdapter { _ ->
            // Handle time selection if needed
        }
        binding.rvTimeSlots.adapter = timeAdapter
    }

    private fun setupSeats() {
        seatAdapter = SeatAdapter { selectedSeats -> updatePrice(selectedSeats) }
        binding.rvSeats.adapter = seatAdapter
    }

    private fun updatePrice(
            selectedSeats: List<com.example.mobileapp.feature.ticket.ui.adapter.Seat>
    ) {
        val total = selectedSeats.sumOf { it.price }
        val formatter = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"))
        binding.tvTotalPrice.text = formatter.format(total)

        if (selectedSeats.isEmpty()) {
            binding.tvSeatInfo.text = "Chưa chọn ghế"
            binding.tvSeatInfo.setTextColor(
                    requireContext().getColor(com.example.mobileapp.R.color.dark_gray)
            )
        } else {
            val seatNames = selectedSeats.joinToString(", ") { it.id }
            binding.tvSeatInfo.text = seatNames
            binding.tvSeatInfo.setTextColor(
                    requireContext().getColor(com.example.mobileapp.R.color.white)
            )
        }
    }

    private fun showBookingConfirmation() {
        val totalText = binding.tvTotalPrice.text.toString()
        if (totalText == "0") {
            Toast.makeText(requireContext(), "Vui lòng chọn ít nhất một ghế!", Toast.LENGTH_SHORT)
                    .show()
            return
        }

        val date = binding.tvSelectedDate.text
        val seats = binding.tvSeatInfo.text

        AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đặt vé")
                .setMessage(
                        "Phim: Spider-Man: No Way Home\nNgày: $date\nGhế: $seats\nTổng tiền: ${totalText}đ"
                )
                .setPositiveButton("Đồng ý") { _, _ ->
                    Toast.makeText(requireContext(), "Đặt vé thành công!", Toast.LENGTH_SHORT)
                            .show()
                    findNavController().navigateUp()
                }
                .setNegativeButton("Hủy", null)
                .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
