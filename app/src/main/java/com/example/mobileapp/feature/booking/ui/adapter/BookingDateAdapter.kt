package com.example.mobileapp.feature.booking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.feature.booking.domain.model.AvailableDate

class BookingDateAdapter(
    private val onDateSelected: (AvailableDate) -> Unit
) : ListAdapter<AvailableDate, BookingDateAdapter.DateViewHolder>(DateDiffCallback()) {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedPosition)
    }

    fun selectDate(date: AvailableDate) {
        val newPosition = currentList.indexOfFirst { it.date == date.date }
        if (newPosition != -1 && newPosition != selectedPosition) {
            val previousPosition = selectedPosition
            selectedPosition = newPosition
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    fun getSelectedDate(): AvailableDate? {
        return if (selectedPosition < currentList.size) {
            currentList[selectedPosition]
        } else null
    }

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDayName: TextView = itemView.findViewById(R.id.tv_day_name)
        private val tvDayNumber: TextView = itemView.findViewById(R.id.tv_day_number)
        private val rootView: View = itemView.findViewById(R.id.root_view)

        fun bind(date: AvailableDate, isSelected: Boolean) {
            tvDayName.text = date.dayOfWeekShort.uppercase()
            tvDayNumber.text = date.dayNumber.toString()

            rootView.isSelected = isSelected

            if (isSelected) {
                tvDayName.alpha = 0.8f
                tvDayName.setTextColor(itemView.context.getColor(R.color.white))
            } else {
                tvDayName.alpha = 1.0f
                tvDayName.setTextColor(itemView.context.getColor(R.color.dark_gray))
            }

            // Highlight today
            if (date.isToday && !isSelected) {
                rootView.alpha = 1.0f
            }

            itemView.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onDateSelected(date)
            }
        }
    }

    private class DateDiffCallback : DiffUtil.ItemCallback<AvailableDate>() {
        override fun areItemsTheSame(oldItem: AvailableDate, newItem: AvailableDate): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: AvailableDate, newItem: AvailableDate): Boolean {
            return oldItem == newItem
        }
    }
}
