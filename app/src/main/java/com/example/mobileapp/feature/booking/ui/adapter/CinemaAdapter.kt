package com.example.mobileapp.feature.booking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.ItemCinemaBinding
import com.example.mobileapp.feature.booking.domain.model.Cinema

class CinemaAdapter(private val onCinemaClick: (Cinema) -> Unit) :
        ListAdapter<Cinema, CinemaAdapter.CinemaViewHolder>(CinemaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaViewHolder {
        val binding = ItemCinemaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CinemaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CinemaViewHolder(private val binding: ItemCinemaBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(cinema: Cinema) {
            binding.tvCinemaName.text = cinema.name
            binding.tvCinemaAddress.text = cinema.fullAddress

            // Hide distance for now since we don't have location data
            if (cinema.hasLocation) {
                binding.tvDistance.visibility = View.VISIBLE
                // In the future, calculate distance based on user's location
                binding.tvDistance.text = "Location available"
            } else {
                binding.tvDistance.visibility = View.GONE
            }

            binding.root.setOnClickListener { onCinemaClick(cinema) }
        }
    }

    class CinemaDiffCallback : DiffUtil.ItemCallback<Cinema>() {
        override fun areItemsTheSame(oldItem: Cinema, newItem: Cinema): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cinema, newItem: Cinema): Boolean {
            return oldItem == newItem
        }
    }
}
