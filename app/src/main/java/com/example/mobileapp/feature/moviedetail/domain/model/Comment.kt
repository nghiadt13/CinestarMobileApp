package com.example.mobileapp.feature.moviedetail.domain.model

data class Comment(
    val id: String,
    val userId: String,
    val userName: String,
    val userAvatar: String?,
    val rating: Float,
    val comment: String,
    val timestamp: Long,
    val likeCount: Int = 0,
    val isLiked: Boolean = false
) {
    fun getFormattedTime(): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60_000 -> "Vừa xong" // Less than 1 minute
            diff < 3_600_000 -> "${diff / 60_000} phút trước" // Less than 1 hour
            diff < 86_400_000 -> "${diff / 3_600_000} giờ trước" // Less than 1 day
            diff < 604_800_000 -> "${diff / 86_400_000} ngày trước" // Less than 1 week
            diff < 2_592_000_000 -> "${diff / 604_800_000} tuần trước" // Less than 1 month
            else -> "${diff / 2_592_000_000} tháng trước" // Months ago
        }
    }
}
