package com.example.mobileapp.feature.booking.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemCinemaBinding
import com.example.mobileapp.feature.booking.domain.model.Cinema

class CinemaAdapter(private val onCinemaClick: (Cinema) -> Unit) :
        ListAdapter<Cinema, CinemaAdapter.CinemaViewHolder>(CinemaDiffCallback()) {

    private var selectedCinemaId: Long? = null
    private val animatedPositions = mutableSetOf<Int>()

    inner class CinemaViewHolder(private val binding: ItemCinemaBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(cinema: Cinema, position: Int) {
            binding.apply {
                // Set cinema data
                tvCinemaName.text = cinema.name
                tvCinemaAddress.text = cinema.address

                // Load cinema logo
                Glide.with(ivCinemaLogo.context)
                        .load(cinema.logoUrl)
                        .placeholder(R.drawable.ic_movie_24)
                        .error(R.drawable.ic_movie_24)
                        .into(ivCinemaLogo)

                // Update selection state
                val isSelected = cinema.id == selectedCinemaId
                cardCinema.isSelected = isSelected

                // Simple scale animation for selection
                if (isSelected) {
                    cardCinema.animate()
                        .scaleX(1.02f)
                        .scaleY(1.02f)
                        .setDuration(150)
                        .start()
                    ivCheckIcon.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(200).start()
                } else {
                    cardCinema.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                    ivCheckIcon.animate().alpha(0f).scaleX(0.5f).scaleY(0.5f).setDuration(200).start()
                }

                // Fade in animation only on first load
                if (!animatedPositions.contains(position)) {
                    animatedPositions.add(position)
                    val animation = AnimationUtils.loadAnimation(root.context, R.anim.fade_in_up)
                    animation.startOffset = (position * 50).toLong()
                    root.startAnimation(animation)
                }

                // Click listener
                root.setOnClickListener {
                    val previousSelectedId = selectedCinemaId
                    selectedCinemaId = cinema.id

                    // Notify changes for smooth animation
                    if (previousSelectedId != null) {
                        val previousPosition =
                                currentList.indexOfFirst { it.id == previousSelectedId }
                        if (previousPosition != -1) {
                            notifyItemChanged(previousPosition)
                        }
                    }
                    notifyItemChanged(position)

                    onCinemaClick(cinema)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaViewHolder {
        val binding = ItemCinemaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CinemaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    fun clearSelection() {
        selectedCinemaId = null
        animatedPositions.clear()
        notifyDataSetChanged()
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
