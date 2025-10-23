package com.example.mobileapp.feature.homepage.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemBannerImageBinding
import com.example.mobileapp.feature.homepage.domain.model.CarouselItem

class CarouselAdapter(private val onItemClick: (CarouselItem) -> Unit) :
        ListAdapter<CarouselItem, CarouselAdapter.CarouselViewHolder>(CarouselDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
                ItemBannerImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

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

    private class CarouselDiffCallBack : DiffUtil.ItemCallback<CarouselItem>() {
        override fun areItemsTheSame(oldItem: CarouselItem, newItem: CarouselItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CarouselItem, newItem: CarouselItem): Boolean {
            return oldItem == newItem
        }
    }
}
