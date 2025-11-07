package com.example.mobileapp.feature.auth.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.mobileapp.R
import com.example.mobileapp.databinding.FragmentLoginCommonBinding
import com.example.mobileapp.feature.auth.ui.viewmodel.AuthState
import com.example.mobileapp.feature.auth.ui.viewmodel.AuthViewModel
import com.example.mobileapp.ui.extension.setClickablePart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginCommonFragment : Fragment() {
    private var _binding: FragmentLoginCommonBinding? = null
    private val binding
        get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginCommonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeAuthState()
    }

    private fun setupClickListeners() {
        binding.buttonViewBackButton.setOnClickListener { findNavController().navigateUp() }

        binding.textViewSignUp.setClickablePart(
                fullText = "Don't have an account ? Sign up",
                clickableText = "Sign up",
                normalColor = ContextCompat.getColor(requireContext(), R.color.black),
                clickableColor = ContextCompat.getColor(requireContext(), R.color.orange),
                isBoldClickable = true
        ) { findNavController().navigate(R.id.action_loginCommon_to_signUp) }

        binding.buttonViewSignIn.setOnClickListener { handleLogin() }
    }

    private fun handleLogin() {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.editTextEmail.error = "Email is required"
            return
        }

        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password is required"
            return
        }

        authViewModel.login(email, password)
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.authState.collect { state ->
                    when (state) {
                        is AuthState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.buttonViewSignIn.isEnabled = false
                        }
                        is AuthState.Idle -> {
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.buttonViewSignIn.isEnabled = true
                        }
                        is AuthState.LoginSuccess -> {
                            binding.progressBar.visibility = View.GONE
                            binding.buttonViewSignIn.isEnabled = true

                            val user = state.loginResult.user
                            if (user != null) {
                                Toast.makeText(
                                                requireContext(),
                                                "Welcome ${user.displayName}!",
                                                Toast.LENGTH_SHORT
                                        )
                                        .show()

                                findNavController().navigate(R.id.action_loginCommon_to_homePage)
                            }
                        }
                        is AuthState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.buttonViewSignIn.isEnabled = true
                            binding.editTextPassword.text?.clear()

                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG)
                                    .show()
                        }
                        is AuthState.UsersLoaded -> {
                            binding.progressBar.visibility = View.GONE
                            binding.buttonViewSignIn.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
