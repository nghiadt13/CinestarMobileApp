package com.example.mobileapp.feature.ticket.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R

class TimeAdapter(private val onTimeSelected: (String) -> Unit) :
        RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    private val timeSlots = listOf("14:30", "16:45", "19:15", "21:00", "23:30")
    private var selectedPosition = 1 // Default selection

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_time_slot, parent, false)
        return TimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(timeSlots[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = timeSlots.size

    inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime: TextView = itemView.findViewById(R.id.tv_time)

        fun bind(time: String, isSelected: Boolean) {
            tvTime.text = time
            tvTime.isSelected = isSelected

            if (isSelected) {
                tvTime.setTextColor(itemView.context.getColor(R.color.brand_dark))
            } else {
                tvTime.setTextColor(itemView.context.getColor(R.color.dark_gray))
            }

            itemView.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onTimeSelected(time)
            }
        }
    }
}
