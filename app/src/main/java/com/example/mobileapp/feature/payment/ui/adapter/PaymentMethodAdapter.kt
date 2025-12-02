package com.example.mobileapp.feature.payment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemPaymentMethodBinding
import com.example.mobileapp.feature.payment.domain.model.PaymentIconType
import com.example.mobileapp.feature.payment.domain.model.PaymentMethod

class PaymentMethodAdapter(
    private val onPaymentMethodSelected: (PaymentMethod) -> Unit
) : ListAdapter<PaymentMethod, PaymentMethodAdapter.PaymentMethodViewHolder>(PaymentMethodDiffCallback()) {

    inner class PaymentMethodViewHolder(private val binding: ItemPaymentMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(method: PaymentMethod) {
            binding.apply {
                // Set payment method data
                tvPaymentName.text = method.name
                tvPaymentDescription.text = method.description

                // Set icon based on type
                when (method.iconType) {
                    PaymentIconType.VIETQR -> {
                        iconContainer.setBackgroundResource(R.drawable.bg_icon_vietqr)
                        imgPaymentIcon.setImageResource(R.drawable.ic_cgv)
                        imgPaymentIcon.setColorFilter(
                            ContextCompat.getColor(root.context, R.color.vietqr_blue)
                        )
                    }
                    PaymentIconType.MOMO -> {
                        iconContainer.setBackgroundResource(R.drawable.bg_icon_momo)
                        imgPaymentIcon.setImageResource(R.drawable.ic_cgv)
                        imgPaymentIcon.setColorFilter(
                            ContextCompat.getColor(root.context, R.color.white)
                        )
                    }
                    PaymentIconType.ZALOPAY -> {
                        iconContainer.setBackgroundResource(R.drawable.bg_icon_zalopay)
                        imgPaymentIcon.setImageResource(R.drawable.ic_cgv)
                        imgPaymentIcon.setColorFilter(
                            ContextCompat.getColor(root.context, R.color.white)
                        )
                    }
                }

                // Update selection state
                updateSelectionState(method.isSelected)

                // Click listener
                root.setOnClickListener {
                    onPaymentMethodSelected(method)
                }
            }
        }

        private fun updateSelectionState(isSelected: Boolean) {
            binding.apply {
                if (isSelected) {
                    paymentMethodContainer.setBackgroundResource(R.drawable.bg_payment_method_selected)
                    checkIndicator.setBackgroundResource(R.drawable.bg_check_circle_active)
                    imgCheck.visibility = View.VISIBLE

                    // Animate selection
                    paymentMethodContainer.animate()
                        .scaleX(1.02f)
                        .scaleY(1.02f)
                        .translationY(-4f)
                        .setDuration(200)
                        .start()
                } else {
                    paymentMethodContainer.setBackgroundResource(R.drawable.bg_payment_method_normal)
                    checkIndicator.setBackgroundResource(R.drawable.bg_check_circle_inactive)
                    imgCheck.visibility = View.INVISIBLE

                    // Animate deselection
                    paymentMethodContainer.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .translationY(0f)
                        .setDuration(200)
                        .start()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val binding = ItemPaymentMethodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentMethodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PaymentMethodDiffCallback : DiffUtil.ItemCallback<PaymentMethod>() {
        override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
            return oldItem == newItem
        }
    }
}
