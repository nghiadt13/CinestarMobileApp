package com.example.mobileapp.feature.payment.domain.model

data class PaymentSummary(
    val ticketPrice: Long,
    val ticketCount: Int,
    val comboPrice: Long,
    val discount: Long,
    val totalPrice: Long
) {
    companion object {
        fun calculate(
            ticketPrice: Long,
            ticketCount: Int,
            combos: List<ComboItem>,
            discount: Long = 0
        ): PaymentSummary {
            val totalTicketPrice = ticketPrice * ticketCount
            val totalComboPrice = combos.sumOf { it.price * it.quantity }
            val total = totalTicketPrice + totalComboPrice - discount

            return PaymentSummary(
                ticketPrice = totalTicketPrice,
                ticketCount = ticketCount,
                comboPrice = totalComboPrice,
                discount = discount,
                totalPrice = total
            )
        }
    }
}
