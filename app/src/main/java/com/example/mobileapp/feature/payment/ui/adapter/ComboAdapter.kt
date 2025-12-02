package com.example.mobileapp.feature.payment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemComboBinding
import com.example.mobileapp.feature.payment.domain.model.ComboItem

class ComboAdapter(
    private val onQuantityChanged: (comboId: String, change: Int) -> Unit
) : ListAdapter<ComboItem, ComboAdapter.ComboViewHolder>(ComboDiffCallback()) {

    inner class ComboViewHolder(private val binding: ItemComboBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(combo: ComboItem) {
            binding.apply {
                // Set combo data
                tvComboName.text = combo.name
                tvComboDescription.text = combo.description
                tvComboPrice.text = formatPrice(combo.price)
                tvQuantity.text = combo.quantity.toString()

                // Load combo image
                Glide.with(imgCombo.context)
                    .load(combo.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .centerCrop()
                    .into(imgCombo)

                // Quantity controls
                btnDecrease.setOnClickListener {
                    if (combo.quantity > 0) {
                        onQuantityChanged(combo.id, -1)
                    }
                }

                btnIncrease.setOnClickListener {
                    onQuantityChanged(combo.id, 1)
                }

                // Update decrease button appearance based on quantity
                btnDecrease.alpha = if (combo.quantity > 0) 1f else 0.5f
            }
        }

        private fun formatPrice(price: Long): String {
            return String.format("%,d", price).replace(",", ".") + "đ"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComboViewHolder {
        val binding = ItemComboBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ComboViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComboViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ComboDiffCallback : DiffUtil.ItemCallback<ComboItem>() {
        override fun areItemsTheSame(oldItem: ComboItem, newItem: ComboItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ComboItem, newItem: ComboItem): Boolean {
            return oldItem == newItem
        }
    }
}
