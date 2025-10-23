package com.example.mobileapp.feature.homepage.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.ItemHomeCarouselBinding
import com.example.mobileapp.feature.homepage.domain.model.CarouselItem

class HomePageAdapter(private val onCarouselItemClick: (CarouselItem) -> Unit) :
        ListAdapter<HomeItem, RecyclerView.ViewHolder>(HomeItemDiffCallBack()) {

    companion object {
        private const val VIEW_TYPE_CAROUSEL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeItem.CarouselSection -> VIEW_TYPE_CAROUSEL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CAROUSEL -> {
                val binding =
                        ItemHomeCarouselBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent,
                                false
                        )
                CarouselViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CarouselViewHolder -> {
                val item = getItem(position) as HomeItem.CarouselSection
                holder.bind(item.items)
            }
        }
    }

    inner class CarouselViewHolder(private val binding: ItemHomeCarouselBinding) :
            RecyclerView.ViewHolder(binding.root) {

        private val carouselAdapter = CarouselAdapter { carouselItem ->
            onCarouselItemClick(carouselItem)
        }

        init {
            binding.viewPagerBanner.adapter = carouselAdapter
            binding.indicator.setViewPager(binding.viewPagerBanner)
        }

        fun bind(carouselItems: List<CarouselItem>) {
            carouselAdapter.submitList(carouselItems)
        }
    }

    private class HomeItemDiffCallBack : DiffUtil.ItemCallback<HomeItem>() {
        override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return when {
                oldItem is HomeItem.CarouselSection && newItem is HomeItem.CarouselSection ->
                        oldItem.items.firstOrNull()?.id == newItem.items.firstOrNull()?.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return oldItem == newItem
        }
    }
}
