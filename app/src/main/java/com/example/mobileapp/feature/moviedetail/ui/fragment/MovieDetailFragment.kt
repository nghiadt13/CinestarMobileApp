package com.example.mobileapp.feature.moviedetail.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentMovieDetailBinding
import com.example.mobileapp.feature.moviedetail.domain.model.MovieDetail
import com.example.mobileapp.feature.moviedetail.ui.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupWebView()
        observeViewModel()

        // Fetch movie detail with ID from navigation args
        viewModel.fetchMovieDetail(args.movieId.toLong())
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.fetchMovieDetail(args.movieId.toLong())
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
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

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieDetail.collect { movieDetail ->
                movieDetail?.let { bindMovieDetail(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.scrollView.visibility = if (isLoading) View.GONE else View.VISIBLE
                binding.errorView.visibility = View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                if (error != null) {
                    showError(error)
                }
            }
        }
    }

    private fun bindMovieDetail(movie: MovieDetail) {
        binding.apply {
            // Load poster image
            Glide.with(requireContext())
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
            if (!movie.trailerUrl.isNullOrEmpty()) {
                val videoId = extractYouTubeVideoId(movie.trailerUrl)
                if (videoId != null) {
                    loadYouTubeVideoInWebView(videoId)
                    webviewTrailer.visibility = View.VISIBLE
                    tvNoTrailer.visibility = View.GONE
                } else {
                    webviewTrailer.visibility = View.GONE
                    tvNoTrailer.visibility = View.VISIBLE
                }
            } else {
                webviewTrailer.visibility = View.GONE
                tvNoTrailer.visibility = View.VISIBLE
            }

            // Show content
            scrollView.visibility = View.VISIBLE
            errorView.visibility = View.GONE
        }
    }

    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Không rõ"

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
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
        Log.d("MovieDetailFragment", "YouTube video loaded in WebView: $videoId")
    }

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
                    Log.d("MovieDetailFragment", "Extracted video ID: $videoId from URL: $url")
                    return videoId
                }
            }

            Log.w("MovieDetailFragment", "Could not extract video ID from URL: $url")
            null
        } catch (e: Exception) {
            Log.e("MovieDetailFragment", "Error extracting video ID: ${e.message}")
            null
        }
    }

    private fun showError(errorMessage: String) {
        binding.apply {
            scrollView.visibility = View.GONE
            progressBar.visibility = View.GONE
            errorView.visibility = View.VISIBLE
            tvError.text = errorMessage
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Destroy WebView to prevent memory leaks
        binding.webviewTrailer.destroy()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        // Pause WebView when fragment is paused
        binding.webviewTrailer.onPause()
    }

    override fun onResume() {
        super.onResume()
        // Resume WebView when fragment is resumed
        binding.webviewTrailer.onResume()
    }
}
