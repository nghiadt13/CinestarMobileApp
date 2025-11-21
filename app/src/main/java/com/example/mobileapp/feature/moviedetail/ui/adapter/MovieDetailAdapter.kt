package com.example.mobileapp.feature.moviedetail.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.recyclerview.widget.LinearLayoutManager
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
        private const val INITIAL_COMMENTS_DISPLAY = 3
    }

    private val commentAdapter = CommentAdapter()

    /**
     * Setup comments RecyclerView
     */
    fun setupCommentsRecyclerView() {
        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
            isNestedScrollingEnabled = false
        }
    }

    /**
     * Setup WebView for YouTube trailer playback
     */
    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebView() {
        binding.webviewTrailer.apply {
            // ✅ Enable hardware acceleration cho WebView
            setLayerType(View.LAYER_TYPE_HARDWARE, null)

            settings.apply {
                // JavaScript & DOM
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true

                // Layout & Viewport
                loadWithOverviewMode = true
                useWideViewPort = true
                setSupportZoom(false)
                builtInZoomControls = false
                displayZoomControls = false

                // Media & Content
                mediaPlaybackRequiresUserGesture = false
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                allowFileAccess = true
                allowContentAccess = true

                // Cache & Performance
                cacheMode = WebSettings.LOAD_DEFAULT
                setRenderPriority(WebSettings.RenderPriority.HIGH)

                // User Agent - YouTube cần user agent để nhận diện device
                userAgentString = "Mozilla/5.0 (Linux; Android 10; SM-G973F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
            }

            // Custom WebChromeClient để xử lý full screen, permissions, etc
            webChromeClient = object : WebChromeClient() {
                override fun onShowFileChooser(
                    webView: WebView,
                    filePathCallback: android.webkit.ValueCallback<Array<android.net.Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    Log.d(TAG, "File chooser requested")
                    return true
                }
            }

            // Custom WebViewClient để log loading events và errors
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    Log.d(TAG, "✅ Page started loading: $url")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d(TAG, "✅ Page finished loading: $url")
                }

                override fun onReceivedError(view: WebView?, request: android.webkit.WebResourceRequest?, error: android.webkit.WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    Log.e(TAG, "❌ WebView error on ${request?.url}: ${error?.description} (code: ${error?.errorCode})")
                }

                override fun onReceivedHttpError(view: WebView?, request: android.webkit.WebResourceRequest?, errorResponse: android.webkit.WebResourceResponse?) {
                    super.onReceivedHttpError(view, request, errorResponse)
                    Log.e(TAG, "❌ HTTP error on ${request?.url}: ${errorResponse?.statusCode} ${errorResponse?.reasonPhrase}")
                }

                // ✅ QUAN TRỌNG: Cho phép WebView load tất cả URL (bao gồm YouTube iframe)
                override fun shouldOverrideUrlLoading(view: WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                    Log.d(TAG, "shouldOverrideUrlLoading: ${request?.url}")
                    // Return false để WebView tự xử lý, không block navigation
                    return false
                }
            }
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

            // Comments
            handleComments(movie.comments)

            // Show content
            scrollView.visibility = View.VISIBLE
            errorView.visibility = View.GONE
        }
    }

    /**
     * Handle comments display
     */
    private fun handleComments(comments: List<com.example.mobileapp.feature.moviedetail.domain.model.Comment>) {
        binding.apply {
            if (comments.isEmpty()) {
                // Show empty state
                layoutEmptyComments.visibility = View.VISIBLE
                rvComments.visibility = View.GONE
                btnLoadMoreComments.visibility = View.GONE
                tvTotalComments.text = "0 bình luận"
            } else {
                // Show comments
                layoutEmptyComments.visibility = View.GONE
                rvComments.visibility = View.VISIBLE

                // Display initial comments
                val displayComments = if (comments.size > INITIAL_COMMENTS_DISPLAY) {
                    comments.take(INITIAL_COMMENTS_DISPLAY)
                } else {
                    comments
                }

                commentAdapter.submitList(displayComments)

                // Show total count
                tvTotalComments.text = "${comments.size} bình luận"

                // Handle "Load More" button
                if (comments.size > INITIAL_COMMENTS_DISPLAY) {
                    btnLoadMoreComments.visibility = View.VISIBLE
                    btnLoadMoreComments.setOnClickListener {
                        // Load all comments
                        commentAdapter.submitList(comments)
                        btnLoadMoreComments.visibility = View.GONE
                    }
                } else {
                    btnLoadMoreComments.visibility = View.GONE
                }
            }
        }
    }

    /**
     * Handle YouTube trailer display
     */
    private fun handleTrailer(trailerUrl: String?) {
        Log.d(TAG, "handleTrailer called with URL: $trailerUrl")
        binding.apply {
            if (!trailerUrl.isNullOrEmpty()) {
                Log.d(TAG, "Trailer URL is not empty, attempting to extract video ID")
                val videoId = extractYouTubeVideoId(trailerUrl)
                if (videoId != null) {
                    Log.d(TAG, "Successfully extracted video ID: $videoId, loading in WebView")

                    // ✅ FIX: Set visibility TRƯỚC, sau đó đợi layout pass rồi mới load HTML
                    webviewTrailer.visibility = View.VISIBLE
                    tvNoTrailer.visibility = View.GONE

                    // Post to message queue để đảm bảo WebView đã được measure/layout
                    webviewTrailer.post {
                        Log.d(TAG, "WebView dimensions after layout: width=${webviewTrailer.width}, height=${webviewTrailer.height}")
                        loadYouTubeVideoInWebView(videoId)
                    }

                    Log.d(TAG, "WebView visibility set to VISIBLE, waiting for layout")
                } else {
                    Log.w(TAG, "Failed to extract video ID from URL: $trailerUrl")
                    showNoTrailer()
                }
            } else {
                Log.w(TAG, "Trailer URL is null or empty, showing no trailer message")
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
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <meta http-equiv="X-UA-Compatible" content="ie=edge">
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
                    html, body {
                        width: 100%;
                        height: 100%;
                        background-color: #000000;
                        overflow: hidden;
                    }
                    .video-container {
                        position: relative;
                        width: 100%;
                        padding-bottom: 56.25%; /* 16:9 Aspect Ratio */
                        height: 0;
                        overflow: hidden;
                        background: #000;
                    }
                    .video-container iframe {
                        position: absolute;
                        top: 0;
                        left: 0;
                        width: 100%;
                        height: 100%;
                        border: none;
                    }
                </style>
            </head>
            <body>
                <div class="video-container">
                    <iframe
                        src="https://www.youtube.com/embed/$videoId?rel=0&modestbranding=1&playsinline=1&enablejsapi=1"
                        frameborder="0"
                        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                        allowfullscreen>
                    </iframe>
                </div>
            </body>
            </html>
        """.trimIndent()

        Log.d(TAG, "Loading YouTube video with ID: $videoId")
        Log.d(TAG, "Embed URL: https://www.youtube.com/embed/$videoId")

        // ✅ FIX: Dùng https://appassets.androidplatform.net/ - base URL được recommend cho Android WebView
        // Điều này tránh CORS issues và không navigate đến YouTube homepage
        binding.webviewTrailer.loadDataWithBaseURL(
            "https://appassets.androidplatform.net/",
            html,
            "text/html",
            "utf-8",
            null
        )

        Log.d(TAG, "✅ YouTube video HTML loaded successfully for video ID: $videoId")
    }

    /**
     * Extract YouTube video ID from various URL formats
     */
    private fun extractYouTubeVideoId(url: String): String? {
        return try {
            val patterns = listOf(
                "(?<=watch\\?v=)[^&#]+".toRegex(),
                "(?<=youtu.be/)[^&#?]+".toRegex(),
                "(?<=embed/)[^&#?]+".toRegex(),
                "(?<=\\?v=)[^&#]+".toRegex(),
                "(?<=/v/)[^&#?]+".toRegex()
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