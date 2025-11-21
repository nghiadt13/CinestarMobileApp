package com.example.mobileapp.feature.profile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {  
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        // TODO: Load user data
        binding.textViewName.text = "User Name"
        binding.textViewEmail.text = "user@example.com"

        binding.buttonLogout.setOnClickListener {
            // TODO: Clear user session
            // Navigate back to login
            findNavController().navigate(R.id.loginWith3PartyFragment)
        }
    }  

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
