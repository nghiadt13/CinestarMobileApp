package com.example.mobileapp.feature.homepage.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemMembershipTierCardBinding
import com.example.mobileapp.feature.homepage.domain.model.MembershipTierItem

class MembershipTierAdapter(
    private val onTierClick: (MembershipTierItem) -> Unit
) : ListAdapter<MembershipTierItem, MembershipTierAdapter.MembershipTierViewHolder>(MembershipTierDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembershipTierViewHolder {
        val binding = ItemMembershipTierCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MembershipTierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MembershipTierViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MembershipTierViewHolder(
        private val binding: ItemMembershipTierCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MembershipTierItem) {
            Glide.with(binding.imageViewTierIcon.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .timeout(30000)
                .transition(DrawableTransitionOptions.withCrossFade(400))
                .into(binding.imageViewTierIcon)

            binding.textViewTierName.text = item.name
            binding.textViewTierDescription.text = item.description

            binding.root.setOnClickListener {
                onTierClick(item)
            }
        }
    }

    class MembershipTierDiffCallback : DiffUtil.ItemCallback<MembershipTierItem>() {
        override fun areItemsTheSame(oldItem: MembershipTierItem, newItem: MembershipTierItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MembershipTierItem, newItem: MembershipTierItem): Boolean {
            return oldItem == newItem
        }
    }
}
