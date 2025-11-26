package com.example.mobileapp.feature.ticket.ui.fragment

import android.app.AlertDialog
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
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentBookingTicketBinding
import com.example.mobileapp.feature.booking.domain.model.Seat
import com.example.mobileapp.feature.booking.ui.adapter.BookingDateAdapter
import com.example.mobileapp.feature.booking.ui.adapter.BookingSeatAdapter
import com.example.mobileapp.feature.booking.ui.adapter.FormatAdapter
import com.example.mobileapp.feature.booking.ui.adapter.ShowtimeAdapter
import com.example.mobileapp.feature.booking.ui.viewmodel.BookingTicketViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class BookingTicketFragment : Fragment() {

    private var _binding: FragmentBookingTicketBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookingTicketViewModel by viewModels()
    private val args: BookingTicketFragmentArgs by navArgs()

    private lateinit var dateAdapter: BookingDateAdapter
    private lateinit var formatAdapter: FormatAdapter
    private lateinit var showtimeAdapter: ShowtimeAdapter
    private lateinit var seatAdapter: BookingSeatAdapter

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
        setupAdapters()
        observeViewModel()
        loadData()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            viewModel.unlockSeats()
            findNavController().navigateUp()
        }

        binding.tvCinemaName.text = args.cinemaName

        binding.btnBook.setOnClickListener {
            viewModel.createBooking()
        }
    }

    private fun setupAdapters() {
        // Date adapter
        dateAdapter = BookingDateAdapter { date ->
            viewModel.selectDate(date)
            binding.tvSelectedDate.text = date.dayOfWeek.uppercase()
        }
        binding.rvDates.adapter = dateAdapter

        // Format adapter
        formatAdapter = FormatAdapter { format ->
            viewModel.selectFormat(format)
        }
        binding.rvFormats.adapter = formatAdapter

        // Showtime adapter
        showtimeAdapter = ShowtimeAdapter { showtime ->
            viewModel.selectShowtime(showtime)
        }
        binding.rvTimeSlots.adapter = showtimeAdapter

        // Seat adapter
        seatAdapter = BookingSeatAdapter(
            onSeatClicked = { seat -> viewModel.toggleSeatSelection(seat) },
            isSeatSelected = { seatId -> viewModel.isSeatSelected(seatId) }
        )
        binding.rvSeats.adapter = seatAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                // Show/hide loading indicator
                binding.btnBook.isEnabled = !isLoading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                    viewModel.clearError()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookingData.collectLatest { data ->
                data?.let { bookingData ->
                    // Update movie info
                    binding.tvMovieTitle.text = bookingData.movie.title
                    binding.tvMovieMeta.text = bookingData.movie.metaText



                    // Update dates
                    dateAdapter.submitList(bookingData.availableDates)
                    bookingData.getTodayDate()?.let { date ->
                        binding.tvSelectedDate.text = date.dayOfWeek.uppercase()
                    }

                    // Update formats
                    formatAdapter.submitList(bookingData.formats)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedDate.collectLatest { date ->
                date?.let {
                    dateAdapter.selectDate(it)
                    binding.tvSelectedDate.text = it.dayOfWeek.uppercase()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedFormat.collectLatest { format ->
                format?.let {
                    formatAdapter.selectFormat(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showtimes.collectLatest { showtimes ->
                showtimeAdapter.submitList(showtimes)
                showtimeAdapter.clearSelection()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.seatMap.collectLatest { seatMap ->
                seatMap?.let {
                    // Update grid layout span count based on columns
                    (binding.rvSeats.layoutManager as? GridLayoutManager)?.spanCount = it.columns
                    seatAdapter.submitList(it.seats)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedSeats.collectLatest { selectedSeats ->
                updateSeatInfo(selectedSeats)
                // Refresh seat adapter to update selection state
                seatAdapter.notifyDataSetChanged()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalPrice.collectLatest { price ->
                val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
                binding.tvTotalPrice.text = formatter.format(price)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bookingResult.collectLatest { booking ->
                booking?.let {
                    showBookingSuccess(it)
                    viewModel.clearBookingResult()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isBooking.collectLatest { isBooking ->
                binding.btnBook.isEnabled = !isBooking
                binding.btnBook.text = if (isBooking) "Đang xử lý..." else "Đặt vé"
            }
        }
    }

    private fun loadData() {
        viewModel.loadBookingData(args.movieId, args.cinemaId)
    }

    private fun updateSeatInfo(selectedSeats: List<Seat>) {
        if (selectedSeats.isEmpty()) {
            binding.tvSeatInfo.text = "Chưa chọn ghế"
            binding.tvSeatInfo.setTextColor(requireContext().getColor(R.color.dark_gray))
        } else {
            binding.tvSeatInfo.text = viewModel.getSelectedSeatsDisplay()
            binding.tvSeatInfo.setTextColor(requireContext().getColor(R.color.white))
        }
    }

    private fun showBookingSuccess(booking: com.example.mobileapp.feature.booking.domain.model.Booking) {
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))

        AlertDialog.Builder(requireContext())
            .setTitle("Đặt vé thành công!")
            .setMessage(
                """
                Mã đặt vé: ${booking.bookingCode}
                Phim: ${booking.movieTitle}
                Rạp: ${booking.cinemaName}
                Ngày: ${booking.showtimeDate}
                Giờ: ${booking.showtimeTime}
                Phòng: ${booking.screenName}
                Ghế: ${booking.seatsDisplay}
                Tổng tiền: ${formatter.format(booking.totalPrice)}đ

                Vui lòng thanh toán trong 15 phút để giữ ghế.
                """.trimIndent()
            )
            .setPositiveButton("OK") { _, _ ->
                findNavController().navigateUp()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.unlockSeats()
        _binding = null
    }
}
