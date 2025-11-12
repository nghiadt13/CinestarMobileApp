package com.example.mobileapp.feature.homepage.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mobileapp.databinding.FragmentHomeMembershipTierBinding
import com.example.mobileapp.feature.homepage.domain.model.MembershipTierItem
import com.example.mobileapp.feature.homepage.ui.adapter.MembershipTierAdapter
import com.example.mobileapp.feature.homepage.ui.viewmodel.MembershipTierViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class MembershipTierFragment : Fragment() {

    private var _binding: FragmentHomeMembershipTierBinding? = null
    private val binding get() = _binding!!
    private val membershipTierViewModel: MembershipTierViewModel by viewModels()
    private lateinit var membershipTierAdapter: MembershipTierAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeMembershipTierBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("MembershipTierFragment", "onViewCreated called")

        setupViewPager()
        observeViewModel()
    }

    private fun setupViewPager() {
        membershipTierAdapter = MembershipTierAdapter { tierItem ->
            onMembershipTierClick(tierItem)
        }

        binding.viewPagerMembershipTier.apply {
            adapter = membershipTierAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            // Get the RecyclerView inside ViewPager2
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            // Add page transformer for carousel effect
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(16))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.15f
            }

            setPageTransformer(compositePageTransformer)
        }

        Log.d("MembershipTierFragment", "ViewPager setup complete")
    }

    private fun observeViewModel() {
        // Observe membership tier items
        viewLifecycleOwner.lifecycleScope.launch {
            membershipTierViewModel.membershipTiers.collect { tierItems ->
                Log.d("MembershipTierFragment", "Received ${tierItems.size} membership tiers")

                if (tierItems.isNotEmpty()) {
                    membershipTierAdapter.submitList(tierItems)
                    binding.viewPagerMembershipTier.visibility = View.VISIBLE
                    Log.d("MembershipTierFragment", "Membership tiers displayed")
                } else {
                    binding.viewPagerMembershipTier.visibility = View.GONE
                    Log.d("MembershipTierFragment", "No membership tiers to display")
                }
            }
        }

        // Observe loading state
        viewLifecycleOwner.lifecycleScope.launch {
            membershipTierViewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                Log.d("MembershipTierFragment", "Loading state: $isLoading")
            }
        }

        // Observe errors
        viewLifecycleOwner.lifecycleScope.launch {
            membershipTierViewModel.isError.collect { error ->
                error?.let {
                    showError(it)
                    Log.e("MembershipTierFragment", "Error: $it")
                }
            }
        }
    }

    private fun onMembershipTierClick(item: MembershipTierItem) {
        Toast.makeText(requireContext(), "Clicked: ${item.name}", Toast.LENGTH_SHORT).show()
        Log.d("MembershipTierFragment", "Membership tier clicked: ${item.name}")
        // TODO: Navigate to membership tier detail screen
    }

    private fun showError(error: String) {
        Toast.makeText(requireContext(), "Error: $error", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
