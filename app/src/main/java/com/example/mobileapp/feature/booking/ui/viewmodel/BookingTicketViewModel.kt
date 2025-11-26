package com.example.mobileapp.feature.booking.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.feature.booking.domain.model.*
import com.example.mobileapp.feature.booking.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class BookingTicketViewModel @Inject constructor(
    private val getBookingDataUseCase: GetBookingDataUseCase,
    private val getSeatMapUseCase: GetSeatMapUseCase,
    private val createBookingUseCase: CreateBookingUseCase,
    private val lockSeatsUseCase: LockSeatsUseCase,
    private val unlockSeatsUseCase: UnlockSeatsUseCase
) : ViewModel() {

    // Loading states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSeatMapLoading = MutableStateFlow(false)
    val isSeatMapLoading: StateFlow<Boolean> = _isSeatMapLoading.asStateFlow()

    private val _isBooking = MutableStateFlow(false)
    val isBooking: StateFlow<Boolean> = _isBooking.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Booking data
    private val _bookingData = MutableStateFlow<BookingData?>(null)
    val bookingData: StateFlow<BookingData?> = _bookingData.asStateFlow()

    // Selected date
    private val _selectedDate = MutableStateFlow<AvailableDate?>(null)
    val selectedDate: StateFlow<AvailableDate?> = _selectedDate.asStateFlow()

    // Selected format
    private val _selectedFormat = MutableStateFlow<ScreeningFormat?>(null)
    val selectedFormat: StateFlow<ScreeningFormat?> = _selectedFormat.asStateFlow()

    // Filtered showtimes based on selected date and format
    private val _showtimes = MutableStateFlow<List<Showtime>>(emptyList())
    val showtimes: StateFlow<List<Showtime>> = _showtimes.asStateFlow()

    // Selected showtime
    private val _selectedShowtime = MutableStateFlow<Showtime?>(null)
    val selectedShowtime: StateFlow<Showtime?> = _selectedShowtime.asStateFlow()

    // Seat map
    private val _seatMap = MutableStateFlow<SeatMap?>(null)
    val seatMap: StateFlow<SeatMap?> = _seatMap.asStateFlow()

    // Selected seats
    private val _selectedSeats = MutableStateFlow<List<Seat>>(emptyList())
    val selectedSeats: StateFlow<List<Seat>> = _selectedSeats.asStateFlow()

    // Total price
    private val _totalPrice = MutableStateFlow(BigDecimal.ZERO)
    val totalPrice: StateFlow<BigDecimal> = _totalPrice.asStateFlow()

    // Booking result
    private val _bookingResult = MutableStateFlow<Booking?>(null)
    val bookingResult: StateFlow<Booking?> = _bookingResult.asStateFlow()

    // Store IDs for API calls
    private var movieId: Long = 0
    private var cinemaId: Long = 0
    private var userId: Long = 1 // TODO: Get from auth/session

    fun loadBookingData(movieId: Long, cinemaId: Long) {
        this.movieId = movieId
        this.cinemaId = cinemaId

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val data = getBookingDataUseCase(movieId, cinemaId)
                _bookingData.value = data

                // Auto-select default date and format
                val defaultDate = data.getTodayDate()
                val defaultFormat = data.getDefaultFormat()

                _selectedDate.value = defaultDate
                _selectedFormat.value = defaultFormat

                // Update showtimes
                updateShowtimes()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load booking data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectDate(date: AvailableDate) {
        _selectedDate.value = date
        updateShowtimes()
        // Clear selected showtime and seats when date changes
        clearShowtimeSelection()
    }

    fun selectFormat(format: ScreeningFormat) {
        _selectedFormat.value = format
        updateShowtimes()
        // Clear selected showtime and seats when format changes
        clearShowtimeSelection()
    }

    fun selectShowtime(showtime: Showtime) {
        if (showtime.isSoldOut) return

        _selectedShowtime.value = showtime
        loadSeatMap(showtime.id)
    }

    private fun loadSeatMap(showtimeId: Long) {
        viewModelScope.launch {
            _isSeatMapLoading.value = true
            _error.value = null
            _selectedSeats.value = emptyList()
            _totalPrice.value = BigDecimal.ZERO

            try {
                val seatMap = getSeatMapUseCase(showtimeId)
                _seatMap.value = seatMap
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load seat map"
            } finally {
                _isSeatMapLoading.value = false
            }
        }
    }

    fun toggleSeatSelection(seat: Seat) {
        if (!seat.isSelectable && seat.status != SeatStatus.AVAILABLE) return

        val currentSelected = _selectedSeats.value.toMutableList()
        val existingSeat = currentSelected.find { it.seatId == seat.seatId }

        if (existingSeat != null) {
            currentSelected.remove(existingSeat)
        } else {
            currentSelected.add(seat)
        }

        _selectedSeats.value = currentSelected
        calculateTotalPrice()
    }

    fun isSeatSelected(seatId: Long): Boolean {
        return _selectedSeats.value.any { it.seatId == seatId }
    }

    private fun calculateTotalPrice() {
        val total = _selectedSeats.value.fold(BigDecimal.ZERO) { acc, seat ->
            acc + seat.price
        }
        _totalPrice.value = total
    }

    fun createBooking() {
        val showtime = _selectedShowtime.value ?: return
        val seats = _selectedSeats.value
        if (seats.isEmpty()) return

        viewModelScope.launch {
            _isBooking.value = true
            _error.value = null

            try {
                val booking = createBookingUseCase(
                    userId = userId,
                    showtimeId = showtime.id,
                    seatIds = seats.map { it.seatId },
                    totalPrice = _totalPrice.value
                )
                _bookingResult.value = booking
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("409") == true -> "Some seats have been booked by others. Please select different seats."
                    e.message?.contains("400") == true -> "Invalid booking information"
                    else -> e.message ?: "Failed to create booking"
                }
                _error.value = errorMessage

                // Refresh seat map on conflict
                if (e.message?.contains("409") == true) {
                    loadSeatMap(showtime.id)
                }
            } finally {
                _isBooking.value = false
            }
        }
    }

    fun lockSelectedSeats() {
        val showtime = _selectedShowtime.value ?: return
        val seats = _selectedSeats.value
        if (seats.isEmpty()) return

        viewModelScope.launch {
            try {
                lockSeatsUseCase(
                    userId = userId,
                    showtimeId = showtime.id,
                    seatIds = seats.map { it.seatId }
                )
            } catch (e: Exception) {
                // Silent failure for lock - not critical
            }
        }
    }

    fun unlockSeats() {
        val showtime = _selectedShowtime.value ?: return

        viewModelScope.launch {
            try {
                unlockSeatsUseCase(userId, showtime.id)
            } catch (e: Exception) {
                // Silent failure for unlock - not critical
            }
        }
    }

    private fun updateShowtimes() {
        val data = _bookingData.value ?: return
        val date = _selectedDate.value?.date ?: return
        val format = _selectedFormat.value?.code ?: return

        _showtimes.value = data.getShowtimesForDateAndFormat(date, format)
    }

    private fun clearShowtimeSelection() {
        _selectedShowtime.value = null
        _seatMap.value = null
        _selectedSeats.value = emptyList()
        _totalPrice.value = BigDecimal.ZERO
    }

    fun clearError() {
        _error.value = null
    }

    fun clearBookingResult() {
        _bookingResult.value = null
    }

    fun getSelectedSeatsDisplay(): String {
        return _selectedSeats.value.joinToString(", ") { it.displayName }
    }

    override fun onCleared() {
        super.onCleared()
        unlockSeats()
    }
}
