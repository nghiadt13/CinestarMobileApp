package com.example.mobileapp.feature.booking.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.feature.booking.domain.model.Showtime

class ShowtimeAdapter(
    private val onShowtimeSelected: (Showtime) -> Unit
) : ListAdapter<Showtime, ShowtimeAdapter.ShowtimeViewHolder>(ShowtimeDiffCallback()) {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowtimeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_showtime, parent, false) as TextView
        return ShowtimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowtimeViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedPosition)
    }

    fun selectShowtime(showtime: Showtime) {
        val newPosition = currentList.indexOfFirst { it.id == showtime.id }
        if (newPosition != -1 && newPosition != selectedPosition) {
            val previousPosition = selectedPosition
            selectedPosition = newPosition
            if (previousPosition != -1) notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    fun clearSelection() {
        val previousPosition = selectedPosition
        selectedPosition = -1
        if (previousPosition != -1) notifyItemChanged(previousPosition)
    }

    inner class ShowtimeViewHolder(private val tvTime: TextView) : RecyclerView.ViewHolder(tvTime) {

        fun bind(showtime: Showtime, isSelected: Boolean) {
            tvTime.text = showtime.time

            // Handle sold out state
            if (showtime.isSoldOut) {
                tvTime.alpha = 0.5f
                tvTime.isEnabled = false
            } else {
                tvTime.alpha = 1.0f
                tvTime.isEnabled = true
            }

            // Selection state
            if (isSelected) {
                tvTime.setTextColor(ContextCompat.getColor(tvTime.context, R.color.brand_dark))
                tvTime.setBackgroundResource(R.drawable.bg_showtime_selected)
            } else {
                tvTime.setTextColor(ContextCompat.getColor(tvTime.context, R.color.dark_gray))
                tvTime.setBackgroundResource(R.drawable.bg_showtime_normal)
            }

            tvTime.setOnClickListener {
                if (!showtime.isSoldOut) {
                    val previousSelected = selectedPosition
                    selectedPosition = adapterPosition
                    if (previousSelected != -1) notifyItemChanged(previousSelected)
                    notifyItemChanged(selectedPosition)
                    onShowtimeSelected(showtime)
                }
            }
        }
    }

    private class ShowtimeDiffCallback : DiffUtil.ItemCallback<Showtime>() {
        override fun areItemsTheSame(oldItem: Showtime, newItem: Showtime): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Showtime, newItem: Showtime): Boolean {
            return oldItem == newItem
        }
    }
}
