package com.example.mobileapp.feature.auth.ui.activity.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivitySignInCommonBinding
import com.example.mobileapp.feature.auth.ui.activity.common.SignUp
import com.example.mobileapp.ui.extension.setClickablePart

class LoginCommon : AppCompatActivity() {
    private lateinit var binding: ActivitySignInCommonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý back button - quay về trang trước
        binding.buttonViewBackButton.setOnClickListener { finish() }

        binding.textViewSignUp.setClickablePart(
            fullText = "Don't have an account ? Sign up",
            clickableText = "Sign up",
            normalColor = ContextCompat.getColor(this, R.color.black),
            clickableColor = ContextCompat.getColor(this, R.color.orange),
            isBoldClickable = true
        ) {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}
