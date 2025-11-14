package com.example.mobileapp.feature.moviedetail.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentMovieDetailBinding
import com.example.mobileapp.feature.moviedetail.domain.model.MovieDetail
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Adapter class to handle binding MovieDetail data to views
 * This separates the view binding logic from the Fragment
 */
class MovieDetailAdapter(
    private val context: Context,
    private val binding: FragmentMovieDetailBinding
) {

    companion object {
        private const val TAG = "MovieDetailAdapter"
    }

    /**
     * Setup WebView for YouTube trailer playback
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebView() {
        binding.webviewTrailer.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                mediaPlaybackRequiresUserGesture = false
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
        }
    }

    fun bindMovieDetail(movie: MovieDetail) {
        binding.apply {
            // Load poster image
            Glide.with(context)
                .load(movie.posterUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(ivPoster)

            // Basic info
            tvTitle.text = movie.title
            tvStatus.text = movie.status.toDisplayText()

            // Rating
            tvRating.text = movie.ratingText
            tvRatingCount.text = "(${movie.ratingCount} đánh giá)"

            // Movie info
            tvDuration.text = movie.durationText
            tvReleaseDate.text = formatDate(movie.releaseDate)
            tvGenres.text = movie.genreNames
            tvFormats.text = movie.formatCodes

            // Synopsis
            tvSynopsis.text = movie.synopsis ?: "Không có thông tin"

            // Trailer - Load YouTube video
            handleTrailer(movie.trailerUrl)

            // Show content
            scrollView.visibility = View.VISIBLE
            errorView.visibility = View.GONE
        }
    }

    /**
     * Handle YouTube trailer display
     */
    private fun handleTrailer(trailerUrl: String?) {
        binding.apply {
            if (!trailerUrl.isNullOrEmpty()) {
                val videoId = extractYouTubeVideoId(trailerUrl)
                if (videoId != null) {
                    loadYouTubeVideoInWebView(videoId)
                    webviewTrailer.visibility = View.VISIBLE
                    tvNoTrailer.visibility = View.GONE
                } else {
                    showNoTrailer()
                }
            } else {
                showNoTrailer()
            }
        }
    }

    /**
     * Show "no trailer available" message
     */
    private fun showNoTrailer() {
        binding.apply {
            webviewTrailer.visibility = View.GONE
            tvNoTrailer.visibility = View.VISIBLE
        }
    }

    /**
     * Format date from yyyy-MM-dd to dd/MM/yyyy
     */

    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Không rõ"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            Log.e(TAG, "Error formatting date: ${e.message}")
            dateString
        }
    }

    private fun loadYouTubeVideoInWebView(videoId: String) {
        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        background-color: #000000;
                    }
                    .video-container {
                        position: relative;
                        width: 100%;
                        padding-bottom: 56.25%; /* 16:9 aspect ratio */
                        height: 0;
                        overflow: hidden;
                    }
                    .video-container iframe {
                        position: absolute;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        border: 0;
                    }
                </style>
            </head>
            <body>
                <div class="video-container">
                    <iframe
                        src="https://www.youtube.com/embed/$videoId?autoplay=0&rel=0&modestbranding=1&playsinline=1"
                        frameborder="0"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                        allowfullscreen>
                    </iframe>
                </div>
            </body>
            </html>
        """.trimIndent()

        binding.webviewTrailer.loadDataWithBaseURL(
            "https://www.youtube.com",
            html,
            "text/html",
            "utf-8",
            null
        )
        Log.d(TAG, "YouTube video loaded in WebView: $videoId")
    }

    /**
     * Extract YouTube video ID from various URL formats
     */
    private fun extractYouTubeVideoId(url: String): String? {
        return try {
            // Support various YouTube URL formats:
            // - https://www.youtube.com/watch?v=VIDEO_ID
            // - https://youtu.be/VIDEO_ID
            // - https://www.youtube.com/embed/VIDEO_ID
            // - https://m.youtube.com/watch?v=VIDEO_ID

            val patterns = listOf(
                "(?<=watch\\?v=)[^&#]+".toRegex(),      // youtube.com/watch?v=VIDEO_ID
                "(?<=youtu.be/)[^&#?]+".toRegex(),      // youtu.be/VIDEO_ID
                "(?<=embed/)[^&#?]+".toRegex(),         // youtube.com/embed/VIDEO_ID
                "(?<=\\?v=)[^&#]+".toRegex(),           // any domain with ?v=VIDEO_ID
                "(?<=/v/)[^&#?]+".toRegex()             // youtube.com/v/VIDEO_ID
            )

            for (pattern in patterns) {
                val matchResult = pattern.find(url)
                if (matchResult != null) {
                    val videoId = matchResult.value
                    Log.d(TAG, "Extracted video ID: $videoId from URL: $url")
                    return videoId
                }
            }

            Log.w(TAG, "Could not extract video ID from URL: $url")
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting video ID: ${e.message}")
            null
        }
    }

    /**
     * Show error view with message
     */
    fun showError(errorMessage: String) {
        binding.apply {
            scrollView.visibility = View.GONE
            progressBar.visibility = View.GONE
            errorView.visibility = View.VISIBLE
            tvError.text = errorMessage
        }
    }

    /**
     *
     * Pause WebView (call from Fragment's onPause)
     */
    fun pauseWebView() {
        binding.webviewTrailer.onPause()
    }

    /**
     * Resume WebView (call from Fragment's onResume)
     */
    fun resumeWebView() {
        binding.webviewTrailer.onResume()
    }

    /**
     * Destroy WebView (call from Fragment's onDestroyView)
     */
    fun destroyWebView() {
        binding.webviewTrailer.destroy()
    }
}
