package com.example.mobileapp.feature.ticket.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateAdapter(private val onDateSelected: (Date) -> Unit) :
        RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    private val dates = mutableListOf<Date>()
    private var selectedPosition = 0

    init {
        val calendar = Calendar.getInstance()
        for (i in 0 until 14) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(dates[position], position == selectedPosition)
    }

    override fun getItemCount(): Int = dates.size

    fun getSelectedDate(): Date = dates[selectedPosition]

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDayName: TextView = itemView.findViewById(R.id.tv_day_name)
        private val tvDayNumber: TextView = itemView.findViewById(R.id.tv_day_number)
        private val rootView: View = itemView.findViewById(R.id.root_view)

        fun bind(date: Date, isSelected: Boolean) {
            val dayNameFormat = SimpleDateFormat("EEE", Locale("vi", "VN"))
            val dayNumberFormat = SimpleDateFormat("dd", Locale.getDefault())

            tvDayName.text = dayNameFormat.format(date).uppercase()
            tvDayNumber.text = dayNumberFormat.format(date)

            rootView.isSelected = isSelected

            if (isSelected) {
                tvDayName.alpha = 0.8f
                tvDayName.setTextColor(itemView.context.getColor(R.color.white))
            } else {
                tvDayName.alpha = 1.0f
                tvDayName.setTextColor(itemView.context.getColor(R.color.dark_gray))
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
}
