package com.example.mobileapp.feature.payment.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp.feature.payment.domain.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor() : ViewModel() {

    // Loading states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isProcessingPayment = MutableStateFlow(false)
    val isProcessingPayment: StateFlow<Boolean> = _isProcessingPayment.asStateFlow()

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Booking info
    private val _bookingInfo = MutableStateFlow<BookingInfo?>(null)
    val bookingInfo: StateFlow<BookingInfo?> = _bookingInfo.asStateFlow()

    // Combos
    private val _combos = MutableStateFlow<List<ComboItem>>(emptyList())
    val combos: StateFlow<List<ComboItem>> = _combos.asStateFlow()

    // Payment methods
    private val _paymentMethods = MutableStateFlow<List<PaymentMethod>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethod>> = _paymentMethods.asStateFlow()

    // Selected payment method
    private val _selectedPaymentMethod = MutableStateFlow<PaymentMethod?>(null)
    val selectedPaymentMethod: StateFlow<PaymentMethod?> = _selectedPaymentMethod.asStateFlow()

    // Payment summary
    private val _paymentSummary = MutableStateFlow<PaymentSummary?>(null)
    val paymentSummary: StateFlow<PaymentSummary?> = _paymentSummary.asStateFlow()

    // Payment success
    private val _paymentSuccess = MutableStateFlow(false)
    val paymentSuccess: StateFlow<Boolean> = _paymentSuccess.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        viewModelScope.launch {
            _isLoading.value = true

            // Simulate API delay
            delay(500)

            // Mock booking info
            _bookingInfo.value = BookingInfo(
                movieTitle = "Avengers: Endgame",
                moviePosterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
                genre = "Hành động",
                format = "2D Phụ đề",
                duration = "181 phút",
                cinemaName = "CGV Vincom Center",
                showtime = "19:30",
                showdate = "Hôm nay, 26/11",
                seats = listOf("F12", "F13"),
                room = "Phòng chiếu 05",
                ticketPrice = 90000,
                ticketCount = 2
            )

            // Mock combos
            _combos.value = listOf(
                ComboItem(
                    id = "combo_1",
                    name = "Combo Couple 2 Ngăn",
                    description = "1 Bắp lớn 2 vị + 2 Coca lớn tươi mát lạnh.",
                    price = 109000,
                    imageUrl = "https://images.unsplash.com/photo-1585647347483-22b66260dfff?auto=format&fit=crop&w=200&q=80",
                    quantity = 0
                ),
                ComboItem(
                    id = "combo_2",
                    name = "Combo Solo Năng Lượng",
                    description = "1 Bắp ngọt vừa + 1 Pepsi vừa.",
                    price = 79000,
                    imageUrl = "https://images.unsplash.com/photo-1576158187530-98706e75b285?auto=format&fit=crop&w=200&q=80",
                    quantity = 1
                )
            )

            // Mock payment methods
            val methods = listOf(
                PaymentMethod(
                    id = "vietqr",
                    name = "VietQR",
                    description = "Quét mã QR ngân hàng",
                    iconType = PaymentIconType.VIETQR,
                    isSelected = true
                ),
                PaymentMethod(
                    id = "momo",
                    name = "MoMo",
                    description = "Ví điện tử MoMo",
                    iconType = PaymentIconType.MOMO,
                    isSelected = false
                ),
                PaymentMethod(
                    id = "zalopay",
                    name = "ZaloPay",
                    description = "Ví ZaloPay",
                    iconType = PaymentIconType.ZALOPAY,
                    isSelected = false
                )
            )
            _paymentMethods.value = methods
            _selectedPaymentMethod.value = methods.first()

            // Calculate initial summary
            updatePaymentSummary()

            _isLoading.value = false
        }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _selectedPaymentMethod.value = method
        _paymentMethods.value = _paymentMethods.value.map {
            it.copy(isSelected = it.id == method.id)
        }
    }

    fun updateComboQuantity(comboId: String, change: Int) {
        _combos.value = _combos.value.map { combo ->
            if (combo.id == comboId) {
                val newQuantity = (combo.quantity + change).coerceAtLeast(0)
                combo.copy(quantity = newQuantity)
            } else {
                combo
            }
        }
        updatePaymentSummary()
    }

    private fun updatePaymentSummary() {
        val booking = _bookingInfo.value ?: return
        val discount = 15000L // Mock discount

        _paymentSummary.value = PaymentSummary.calculate(
            ticketPrice = booking.ticketPrice,
            ticketCount = booking.ticketCount,
            combos = _combos.value,
            discount = discount
        )
    }

    fun processPayment() {
        if (_selectedPaymentMethod.value == null) {
            _error.value = "Vui lòng chọn phương thức thanh toán"
            return
        }

        viewModelScope.launch {
            _isProcessingPayment.value = true
            _error.value = null

            // Simulate payment processing
            delay(2000)

            // Mock success
            _paymentSuccess.value = true
            _isProcessingPayment.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun resetPaymentSuccess() {
        _paymentSuccess.value = false
    }

    // Format price to Vietnamese currency
    fun formatPrice(price: Long): String {
        return String.format("%,d", price).replace(",", ".") + "đ"
    }
}
