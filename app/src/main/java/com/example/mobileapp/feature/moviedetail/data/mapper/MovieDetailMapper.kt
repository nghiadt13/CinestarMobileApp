package com.example.mobileapp.feature.moviedetail.data.mapper

import com.example.mobileapp.feature.moviedetail.data.remote.dto.MovieDetailDto
import com.example.mobileapp.feature.moviedetail.domain.model.Comment
import com.example.mobileapp.feature.moviedetail.domain.model.MovieDetail

object MovieDetailMapper {
    fun toDomain(dto: MovieDetailDto): MovieDetail {
        return MovieDetail(
            id = dto.id,
            title = dto.title,
            synopsis = dto.synopsis,
            durationMin = dto.durationMin,
            releaseDate = dto.releaseDate,
            status = dto.status,
            posterUrl = dto.posterUrl,
            trailerUrl = dto.trailerUrl,
            ratingAvg = dto.ratingAvg,
            ratingCount = dto.ratingCount,
            genres = dto.genres,
            formats = dto.formats,
            comments = generateMockComments()
        )
    }

    /**
     * Generate mock comments for testing
     * TODO: Replace with actual API data when available
     */
    private fun generateMockComments(): List<Comment> {
        val currentTime = System.currentTimeMillis()
        return listOf(
            Comment(
                id = "1",
                userId = "user1",
                userName = "Nguyễn Văn A",
                userAvatar = null,
                rating = 4.5f,
                comment = "Phim rất hay, diễn viên diễn xuất tốt. Cảnh quay đẹp, kịch bản chặt chẽ. Rất đáng xem!",
                timestamp = currentTime - 7200000, // 2 hours ago
                likeCount = 15,
                isLiked = false
            ),
            Comment(
                id = "2",
                userId = "user2",
                userName = "Trần Thị B",
                userAvatar = null,
                rating = 5.0f,
                comment = "Tuyệt vời! Đây là một trong những bộ phim hay nhất tôi từng xem.",
                timestamp = currentTime - 86400000, // 1 day ago
                likeCount = 23,
                isLiked = true
            ),
            Comment(
                id = "3",
                userId = "user3",
                userName = "Lê Văn C",
                userAvatar = null,
                rating = 4.0f,
                comment = "Phim khá ổn, nhưng có một vài chi tiết hơi chậm. Nhìn chung vẫn đáng xem.",
                timestamp = currentTime - 259200000, // 3 days ago
                likeCount = 8,
                isLiked = false
            ),
            Comment(
                id = "4",
                userId = "user4",
                userName = "Phạm Thị D",
                userAvatar = null,
                rating = 5.0f,
                comment = "Cảm động quá! Tôi đã khóc khi xem phần cuối.",
                timestamp = currentTime - 604800000, // 1 week ago
                likeCount = 31,
                isLiked = false
            ),
            Comment(
                id = "5",
                userId = "user5",
                userName = "Hoàng Văn E",
                userAvatar = null,
                rating = 3.5f,
                comment = "Phim cũng được, nhưng không hay bằng mong đợi.",
                timestamp = currentTime - 1209600000, // 2 weeks ago
                likeCount = 5,
                isLiked = false
            )
        )
    }
}
