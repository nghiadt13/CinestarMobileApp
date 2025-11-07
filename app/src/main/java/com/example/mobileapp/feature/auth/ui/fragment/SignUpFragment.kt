package com.example.mobileapp.feature.auth.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentSignUpBinding
import com.example.mobileapp.ui.extension.setClickablePart

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonViewBackButton.setOnClickListener { findNavController().navigateUp() }

        binding.textViewSignIn.setClickablePart(
                fullText = "Already have an account ? Sign in",
                clickableText = "Sign in",
                normalColor = ContextCompat.getColor(requireContext(), R.color.black),
                clickableColor = ContextCompat.getColor(requireContext(), R.color.orange),
                isBoldClickable = true
        ) { findNavController().navigate(R.id.action_loginCommon_to_signUp) }

        binding.textViewTerm.setClickablePart(
                fullText = "I agree to CinesStar Terms & Conditions",
                clickableText = "Terms & Conditions",
                normalColor = ContextCompat.getColor(requireContext(), R.color.black),
                clickableColor = ContextCompat.getColor(requireContext(), R.color.orange),
                isBoldClickable = true
        ) {
            // Handle terms click
        }

        binding.buttonViewSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_completeProfile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
