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
            // 1. Tắt clipping trên ViewPager2 (lặp lại từ XML để đảm bảo)
            binding.viewPagerBanner.clipToPadding = false
            binding.viewPagerBanner.clipChildren = false

            // 2. Hiển thị 1 phần của item bên cạnh
            binding.viewPagerBanner.offscreenPageLimit = 3

            val compositeTransformer = androidx.viewpager2.widget.CompositePageTransformer()

            // 4. Thêm MarginTransformer (tạo khoảng cách 20dp giữa các item)
            compositeTransformer.addTransformer(
                androidx.viewpager2.widget.MarginPageTransformer(30) // 20dp margin
            )

            // 5. Thêm ScalingTransformer (tạo hiệu ứng co dãn)
//            compositeTransformer.addTransformer { page, position ->
//                val r = 1 - kotlin.math.abs(position)
//                // Co nhỏ item khi nó không ở giữa (scale 85%)
//                page.scaleY = 0.85f + r * 0.15f
//            }

            // 6. Gán transformer cho ViewPager2
            binding.viewPagerBanner.setPageTransformer(compositeTransformer)

            binding.viewPagerBanner.adapter = carouselAdapter

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
