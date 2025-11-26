package com.example.mobileapp.feature.booking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.feature.booking.domain.model.ScreeningFormat

class FormatAdapter(
    private val onFormatSelected: (ScreeningFormat) -> Unit
) : ListAdapter<ScreeningFormat, FormatAdapter.FormatViewHolder>(FormatDiffCallback()) {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_format_chip, parent, false)
        return FormatViewHolder(view)
    }

    override fun onBindViewHolder(holder: FormatViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedPosition)
    }

    override fun submitList(list: List<ScreeningFormat>?) {
        super.submitList(list)
        // Auto-select default format
        list?.let { formats ->
            val defaultIndex = formats.indexOfFirst { it.isDefault }
            if (defaultIndex != -1 && selectedPosition == -1) {
                selectedPosition = defaultIndex
            }
        }
    }

    fun selectFormat(format: ScreeningFormat) {
        val newPosition = currentList.indexOfFirst { it.code == format.code }
        if (newPosition != -1 && newPosition != selectedPosition) {
            val previousPosition = selectedPosition
            selectedPosition = newPosition
            if (previousPosition != -1) notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    inner class FormatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFormat: TextView = itemView.findViewById(R.id.tv_format)

        fun bind(format: ScreeningFormat, isSelected: Boolean) {
            tvFormat.text = format.name

            if (isSelected) {
                tvFormat.setBackgroundResource(R.drawable.bg_format_chip_selected)
                tvFormat.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                tvFormat.setBackgroundResource(R.drawable.bg_format_chip_normal)
                tvFormat.setTextColor(ContextCompat.getColor(itemView.context, R.color.dark_gray))
            }

            itemView.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = adapterPosition
                if (previousSelected != -1) notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onFormatSelected(format)
            }
        }
    }

    private class FormatDiffCallback : DiffUtil.ItemCallback<ScreeningFormat>() {
        override fun areItemsTheSame(oldItem: ScreeningFormat, newItem: ScreeningFormat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ScreeningFormat, newItem: ScreeningFormat): Boolean {
            return oldItem == newItem
        }
    }
}
