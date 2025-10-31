package com.example.mobileapp.feature.homepage.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.databinding.FragmentHomeCarouselBinding
import com.example.mobileapp.databinding.FragmentHomeMovieBinding
import com.example.mobileapp.feature.homepage.domain.model.CarouselItem
import com.example.mobileapp.feature.homepage.domain.model.MovieItem

class HomePageAdapter(private val onCarouselItemClick: (CarouselItem) -> Unit) :
        ListAdapter<HomeItem, RecyclerView.ViewHolder>(HomeItemDiffCallBack()) {

    companion object {
        private const val VIEW_TYPE_CAROUSEL = 1
        private const val VIEW_TYPE_MOVIE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeItem.CarouselSection -> VIEW_TYPE_CAROUSEL
            is HomeItem.MovieListSection -> VIEW_TYPE_MOVIE

            else -> throw IllegalArgumentException("Unknown item")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CAROUSEL -> {
                val binding =
                    FragmentHomeCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CarouselViewHolder(binding)
            }

            VIEW_TYPE_MOVIE -> {
                val binding = FragmentHomeMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieSectionViewHolder(binding)
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

            is MovieSectionViewHolder -> {
                val movieItem = getItem(position) as HomeItem.MovieListSection
                holder.bind(movieItem.items)
            }
        }
    }

    inner class CarouselViewHolder(private val binding: FragmentHomeCarouselBinding) : RecyclerView.ViewHolder(binding.root) {

        private val carouselAdapter = CarouselAdapter { carouselItem ->
            onCarouselItemClick(carouselItem)
        }

        init {
            // 1. Tắt clipping trên ViewPager2 (lặp lại từ XML để đảm bảo)
            binding.viewPagerBanner.clipToPadding = false
            binding.viewPagerBanner.clipChildren = false

            // 2. Bật overscroll mode để có hiệu ứng kéo giãn
            binding.viewPagerBanner.overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS

            // 3. Đảm bảo RecyclerView bên trong cũng có overscroll
            binding.viewPagerBanner.getChildAt(0)?.let { recyclerView ->
                if (recyclerView is RecyclerView) {
                    recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
                    // Cho phép RecyclerView kéo toàn bộ content theo
                    recyclerView.clipToPadding = false
                    recyclerView.clipChildren = false
                }
            }

            // 4. Hiển thị 1 phần của item bên cạnh
            binding.viewPagerBanner.offscreenPageLimit = 3

            val compositeTransformer = androidx.viewpager2.widget.CompositePageTransformer()

            // 5. Thêm MarginTransformer (tạo khoảng cách giữa các item)
            compositeTransformer.addTransformer(
                    androidx.viewpager2.widget.MarginPageTransformer(30)
            )

            // 6. Gán transformer cho ViewPager2
            binding.viewPagerBanner.setPageTransformer(compositeTransformer)

            binding.viewPagerBanner.adapter = carouselAdapter
        }

        fun bind(carouselItems: List<CarouselItem>) {
            carouselAdapter.submitList(carouselItems)
        }
    }

    inner class MovieSectionViewHolder(private val binding: FragmentHomeMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        private val movieAdapter = MovieAdapter {

        }

        init {

        }

        fun bind(movieItems : List<MovieItem>) {
            movieAdapter.submitList(movieItems)
        }
    }

    private class HomeItemDiffCallBack : DiffUtil.ItemCallback<HomeItem>() {
        override fun areItemsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return when {
                oldItem is HomeItem.CarouselSection && newItem is HomeItem.CarouselSection ->
                        oldItem.items.firstOrNull()?.id == newItem.items.firstOrNull()?.id

                oldItem is HomeItem.MovieListSection && newItem is HomeItem.MovieListSection ->
                    oldItem.items.firstOrNull()?.id == newItem.items.firstOrNull()?.id

                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: HomeItem, newItem: HomeItem): Boolean {
            return oldItem == newItem
        }
    }
}
