package com.example.mobileapp.feature.homepage.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ItemBannerImageBinding


 class BannerAdapter(private val images: List<Int>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

     private lateinit var binding: ItemBannerImageBinding

    inner class BannerViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannerViewHolder {
        val imageView = LayoutInflater.from(parent.context).inflate(R.layout.item_banner_image,parent,false) as ImageView
        return BannerViewHolder(imageView)
    }

    override fun onBindViewHolder(
        holder: BannerViewHolder,
        position: Int
    ) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int = images.size


}