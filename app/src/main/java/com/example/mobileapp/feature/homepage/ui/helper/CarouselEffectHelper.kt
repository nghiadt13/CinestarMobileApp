package com.example.mobileapp.feature.homepage.ui.helper

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlin.math.abs
import kotlin.math.min

/**
 * Helper class to create carousel effect for RecyclerView
 * - Center item will be scaled up and elevated
 * - Side items will be scaled down and translated down
 */
class CarouselEffectHelper(
    private val scaleDownBy: Float = 0.15f,
    private val translationYBy: Float = 60f
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val midpoint = recyclerView.width / 2f
        val d0 = 0f
        val d1 = scaleDownBy * midpoint
        val s0 = 1f
        val s1 = 1f - scaleDownBy

        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val childMidpoint = (child.x + child.width / 2f)
            val d = min(d1, abs(midpoint - childMidpoint))
            val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)

            // Scale the child
            child.scaleX = scale
            child.scaleY = scale

            // Translation Y effect - items on side move down
            val translationY = (1f - scale) * translationYBy
            child.translationY = translationY

            // Elevation effect - center item has higher elevation
            if (child is MaterialCardView) {
                // For MaterialCardView, use cardElevation
                child.cardElevation = scale * 12f
            } else {
                // For regular views, use standard elevation
                child.elevation = scale * 12f
            }

            // Keep full opacity for all items
            child.alpha = 1.0f
        }
    }
}
