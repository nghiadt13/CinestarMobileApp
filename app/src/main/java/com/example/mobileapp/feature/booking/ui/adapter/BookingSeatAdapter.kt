package com.example.mobileapp.feature.booking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.feature.booking.domain.model.Seat
import com.example.mobileapp.feature.booking.domain.model.SeatStatus
import com.example.mobileapp.feature.booking.domain.model.SeatType

class BookingSeatAdapter(
    private val onSeatClicked: (Seat) -> Unit,
    private val isSeatSelected: (Long) -> Boolean
) : ListAdapter<Seat, BookingSeatAdapter.SeatViewHolder>(SeatDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSeat: TextView = itemView.findViewById(R.id.tv_seat)

        fun bind(seat: Seat) {
            val isSelected = isSeatSelected(seat.seatId)

            // Display text
            tvSeat.text = when {
                seat.status == SeatStatus.BOOKED -> "✕"
                seat.status == SeatStatus.LOCKED -> "✕"
                seat.status == SeatStatus.BLOCKED -> ""
                else -> seat.id
            }

            // Determine background based on status and type
            val background = when {
                isSelected -> R.drawable.bg_seat_selected
                seat.status == SeatStatus.BOOKED -> R.drawable.bg_seat_booked
                seat.status == SeatStatus.LOCKED -> R.drawable.bg_seat_locked
                seat.status == SeatStatus.BLOCKED -> R.drawable.bg_seat_blocked
                seat.type == SeatType.VIP -> R.drawable.bg_seat_vip
                seat.type == SeatType.COUPLE -> R.drawable.bg_seat_couple
                seat.type == SeatType.DELUXE -> R.drawable.bg_seat_deluxe
                else -> R.drawable.bg_seat_available
            }
            tvSeat.setBackgroundResource(background)

            // Text color based on state
            val textColor = when {
                isSelected -> itemView.context.getColor(R.color.white)
                seat.status == SeatStatus.BOOKED -> itemView.context.getColor(R.color.seat_booked)
                seat.status == SeatStatus.LOCKED -> itemView.context.getColor(R.color.seat_booked)
                else -> itemView.context.getColor(android.R.color.transparent)
            }
            tvSeat.setTextColor(textColor)

            // Enable/disable based on availability
            val isClickable = seat.status == SeatStatus.AVAILABLE || isSelected
            itemView.isEnabled = isClickable
            tvSeat.isEnabled = isClickable

            itemView.setOnClickListener {
                if (seat.status == SeatStatus.AVAILABLE || isSelected) {
                    onSeatClicked(seat)
                }
            }
        }
    }

    private class SeatDiffCallback : DiffUtil.ItemCallback<Seat>() {
        override fun areItemsTheSame(oldItem: Seat, newItem: Seat): Boolean {
            return oldItem.seatId == newItem.seatId
        }

        override fun areContentsTheSame(oldItem: Seat, newItem: Seat): Boolean {
            return oldItem == newItem
        }
    }
}
