package com.example.mobileapp.feature.homepage.ui.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemMovieCardBinding
import com.example.mobileapp.feature.homepage.domain.model.MovieItem

class MovieAdapter(private val onMovieClick : (MovieItem) -> (Unit)) :
    ListAdapter<MovieItem, MovieAdapter.MovieViewHolder>(MovieDiffCallBack()) {

    companion object {
        // Large number for infinite scrolling effect
        private const val INFINITE_SCROLL_MULTIPLIER = 1000
    }

    override fun getItemCount(): Int {
        // Return a very large number for infinite scrolling
        val realCount = super.getItemCount()
        return if (realCount == 0) 0 else realCount * INFINITE_SCROLL_MULTIPLIER
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MovieViewHolder {
        val binding = ItemMovieCardBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val realItemCount = super.getItemCount()
        if (realItemCount > 0) {
            // Use modulo to get the actual item position
            val actualPosition = position % realItemCount
            holder.bind(getItem(actualPosition))
        }
    }

    fun getRealItemCount(): Int = super.getItemCount()

    inner class MovieViewHolder(private  val binding: ItemMovieCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieItem) {
            Glide.with(binding.imageViewMoviePoster.context)
                .load(item.posterUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .timeout(30000)
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .into(binding.imageViewMoviePoster)

            binding.root.setOnClickListener { onMovieClick(item) }
            binding.buttonBooking.setOnClickListener { onMovieClick(item) }

            binding.textViewMovieTitle.text = item.title
            binding.textViewRating.text = item.ratingAvg.toString()
            binding.textViewDuration.text = "${item.durationMin} phút"
            binding.textViewGenre.text = "Thể loại: ${item.genreNames}"
        }
    }
    class MovieDiffCallBack : DiffUtil.ItemCallback<MovieItem>() {
        override fun areItemsTheSame(
            oldItem: MovieItem,
            newItem: MovieItem
        ): Boolean {
           return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: MovieItem,
            newItem: MovieItem
        ): Boolean {
            return  newItem == oldItem
        }

    }
}