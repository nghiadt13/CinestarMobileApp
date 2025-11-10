package com.example.mobileapp.feature.homepage.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemBannerImageBinding
import com.example.mobileapp.feature.homepage.domain.model.CarouselItem

class CarouselAdapter(private val onItemClick: (CarouselItem) -> Unit) :
        RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    private var carouselItems: List<CarouselItem> = emptyList()

    companion object {
        // Large number for infinite scrolling effect
        private const val INFINITE_SCROLL_MULTIPLIER = 1000
    }

    fun submitList(items: List<CarouselItem>) {
        carouselItems = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        // Return a very large number for infinite scrolling
        return if (carouselItems.isEmpty()) 0 else carouselItems.size * INFINITE_SCROLL_MULTIPLIER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
                ItemBannerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        if (carouselItems.isNotEmpty()) {
            // Use modulo to get the actual item position
            val actualPosition = position % carouselItems.size
            holder.bind(carouselItems[actualPosition])
        }
    }

    fun getRealItemCount(): Int = carouselItems.size

    inner class CarouselViewHolder(private val binding: ItemBannerImageBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarouselItem) {
            Glide.with(binding.imageViewBanner.context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .timeout(30000) // 30 seconds timeout
                    .transition(DrawableTransitionOptions.withCrossFade(400))
                    .into(binding.imageViewBanner)

            binding.root.setOnClickListener { onItemClick(item) }
        }
    }
}
