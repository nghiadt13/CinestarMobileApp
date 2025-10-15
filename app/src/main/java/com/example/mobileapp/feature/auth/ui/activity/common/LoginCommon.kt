package com.example.mobileapp.feature.auth.ui.activity.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivitySignInCommonBinding
import com.example.mobileapp.feature.auth.ui.viewmodel.AuthState
import com.example.mobileapp.feature.auth.ui.viewmodel.AuthViewModel
import com.example.mobileapp.feature.homepage.ui.activity.common.HomePage
import com.example.mobileapp.ui.extension.setClickablePart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginCommon : AppCompatActivity() {
    private lateinit var binding: ActivitySignInCommonBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonViewBackButton.setOnClickListener { finish() }

        binding.textViewSignUp.setClickablePart(
                fullText = "Don't have an account ? Sign up",
                clickableText = "Sign up",
                normalColor = ContextCompat.getColor(this, R.color.black),
                clickableColor = ContextCompat.getColor(this, R.color.orange),
                isBoldClickable = true
        ) { startActivity(Intent(this, SignUp::class.java)) }

        binding.buttonViewSignIn.setOnClickListener { handleLogin() }

        observeAuthState()
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
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                                                this@LoginCommon,
                                                "Welcome ${user.displayName}!",
                                                Toast.LENGTH_SHORT
                                        )
                                        .show()

                                val intent = Intent(this@LoginCommon, HomePage::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        is AuthState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.buttonViewSignIn.isEnabled = true

                            // Clear password field khi login thất bại
                            binding.editTextPassword.text?.clear()

                            Toast.makeText(this@LoginCommon, state.message, Toast.LENGTH_LONG)
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
}
