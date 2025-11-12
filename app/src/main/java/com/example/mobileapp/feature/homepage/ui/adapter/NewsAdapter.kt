package com.example.mobileapp.feature.homepage.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemBannerWithDescriptionBinding
import com.example.mobileapp.feature.homepage.domain.model.NewsItem

class NewsAdapter(
    private val onNewsClick: (NewsItem) -> Unit
) : ListAdapter<NewsItem, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemBannerWithDescriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsViewHolder(
        private val binding: ItemBannerWithDescriptionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: NewsItem) {
            Glide.with(binding.imageViewBanner.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .timeout(30000)
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .into(binding.imageViewBanner)

            binding.textViewNewsTitle.text = item.title

            binding.root.setOnClickListener {
                onNewsClick(item)
            }
        }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }
    }
}
