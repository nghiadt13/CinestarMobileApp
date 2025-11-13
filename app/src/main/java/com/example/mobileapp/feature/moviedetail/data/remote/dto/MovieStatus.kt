package com.example.mobileapp.feature.moviedetail.data.remote.dto

import com.google.gson.annotations.SerializedName

enum class MovieStatus {
    @SerializedName("COMING_SOON")
    COMING_SOON,

    @SerializedName("NOW_SHOWING")
    NOW_SHOWING,

    @SerializedName("ENDED")
    ENDED;

    fun toDisplayText(): String {
        return when (this) {
            COMING_SOON -> "Sắp chiếu"
            NOW_SHOWING -> "Đang chiếu"
            ENDED -> "Đã kết thúc"
        }
    }
}
