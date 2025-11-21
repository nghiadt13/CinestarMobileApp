package com.example.mobileapp.feature.ticket.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R

data class Seat(
        val id: String,
        val row: Int,
        val col: Int,
        var status: SeatStatus,
        val isVip: Boolean,
        val price: Int
)

enum class SeatStatus {
    AVAILABLE,
    SELECTED,
    BOOKED
}

class SeatAdapter(private val onSeatSelected: (List<Seat>) -> Unit) :
        RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    private val seats = mutableListOf<Seat>()
    private val selectedSeats = mutableListOf<Seat>()

    init {
        val rows = 8
        val cols = 8
        val soldSeats = listOf("C3", "C4", "D4", "D5", "F1", "F2")
        val vipRows = listOf(5, 6) // F, G (0-indexed)

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val rowName = (65 + r).toChar().toString()
                val seatName = "$rowName${c + 1}"
                val isVip = vipRows.contains(r)
                val status =
                        if (soldSeats.contains(seatName)) SeatStatus.BOOKED
                        else SeatStatus.AVAILABLE
                val price = if (isVip) 110000 else 85000

                seats.add(Seat(seatName, r, c, status, isVip, price))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        holder.bind(seats[position])
    }

    override fun getItemCount(): Int = seats.size

    inner class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSeat: TextView = itemView.findViewById(R.id.tv_seat)

        fun bind(seat: Seat) {
            tvSeat.text = if (seat.status == SeatStatus.BOOKED) "✕" else seat.id

            when (seat.status) {
                SeatStatus.AVAILABLE -> {
                    tvSeat.setBackgroundResource(
                            if (seat.isVip) R.drawable.bg_seat_vip else R.drawable.bg_seat_available
                    )
                    tvSeat.setTextColor(itemView.context.getColor(android.R.color.transparent))
                    tvSeat.isEnabled = true
                }
                SeatStatus.SELECTED -> {
                    tvSeat.setBackgroundResource(R.drawable.bg_seat_selected)
                    tvSeat.setTextColor(itemView.context.getColor(R.color.white))
                    tvSeat.isEnabled = true
                }
                SeatStatus.BOOKED -> {
                    tvSeat.setBackgroundResource(R.drawable.bg_seat_booked)
                    tvSeat.setTextColor(
                            itemView.context.getColor(R.color.seat_booked)
                    ) // Darker gray for X
                    tvSeat.isEnabled = false
                }
            }

            itemView.setOnClickListener {
                if (seat.status == SeatStatus.AVAILABLE) {
                    seat.status = SeatStatus.SELECTED
                    selectedSeats.add(seat)
                } else if (seat.status == SeatStatus.SELECTED) {
                    seat.status = SeatStatus.AVAILABLE
                    selectedSeats.remove(seat)
                }
                notifyItemChanged(adapterPosition)
                onSeatSelected(selectedSeats)
            }
        }
    }
}
