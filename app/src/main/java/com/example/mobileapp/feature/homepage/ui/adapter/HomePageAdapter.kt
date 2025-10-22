package com.example.mobileapp.feature.homepage.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

data class CarouselItem( val images: List<Int>)
class HomePageAdapter(private val item: List<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {

    }
}