package com.example.mobileapp.feature.moviedetail.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemCommentBinding
import com.example.mobileapp.feature.moviedetail.domain.model.Comment

class CommentAdapter : ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentViewHolder(
        private val binding: ItemCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            with(binding) {
                // User info
                tvCommentUserName.text = comment.userName
                tvCommentTime.text = comment.getFormattedTime()

                // Avatar
                if (!comment.userAvatar.isNullOrEmpty()) {
                    Glide.with(ivCommentAvatar.context)
                        .load(comment.userAvatar)
                        .placeholder(R.drawable.ic_user_placeholder)
                        .error(R.drawable.ic_user_placeholder)
                        .into(ivCommentAvatar)
                } else {
                    ivCommentAvatar.setImageResource(R.drawable.ic_user_placeholder)
                }

                // Rating
                if (comment.rating > 0) {
                    ratingBarComment.visibility = View.VISIBLE
                    tvRatingValue.visibility = View.VISIBLE
                    ratingBarComment.rating = comment.rating
                    tvRatingValue.text = String.format("%.1f", comment.rating)
                } else {
                    ratingBarComment.visibility = View.GONE
                    tvRatingValue.visibility = View.GONE
                }

                // Comment text
                tvCommentText.text = comment.comment
            }
        }
    }

    class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }
}
