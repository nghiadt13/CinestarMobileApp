package com.example.mobileapp.feature.homepage.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main coordinator fragment for homepage
 * Chỉ chứa các child fragments, không quản lý business logic
 * Sử dụng ViewBinding để access views
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {

    // ViewBinding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            setupFragments()
        }

    }

    private fun setupFragments() {
        // Chỉ setup fragments lần đầu tiên (tránh duplicate khi rotate)
            childFragmentManager.beginTransaction()
                .replace(R.id.carousel_container, CarouselFragment())
                .replace(R.id.movie_container, MovieFragment())
                .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Prevent memory leaks
        _binding = null
    }
}