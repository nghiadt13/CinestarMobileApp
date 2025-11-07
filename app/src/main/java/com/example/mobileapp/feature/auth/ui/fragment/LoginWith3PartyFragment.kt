package com.example.mobileapp.feature.auth.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentLoginWith3partyBinding
import com.example.mobileapp.ui.extension.setClickablePart

class LoginWith3PartyFragment : Fragment() {
    private var _binding: FragmentLoginWith3partyBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginWith3partyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonViewSignin.setOnClickListener {
            findNavController().navigate(R.id.action_loginWith3Party_to_loginCommon)
        }

        binding.textViewSignup.setClickablePart(
                fullText = "Don't have an account ? Sign up",
                clickableText = "Sign up",
                normalColor = ContextCompat.getColor(requireContext(), R.color.black),
                clickableColor = ContextCompat.getColor(requireContext(), R.color.orange),
                isBoldClickable = true
        ) { findNavController().navigate(R.id.action_loginWith3Party_to_signUp) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
