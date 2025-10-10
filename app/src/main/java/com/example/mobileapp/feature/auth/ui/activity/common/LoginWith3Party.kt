package com.example.mobileapp.feature.auth.ui.activity.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivityLoginBinding
import com.example.mobileapp.ui.extension.setClickablePart

class LoginWith3Party : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý sự kiện click button Sign in with password --> redirect to LoginCommon
        binding.buttonViewSignin.setOnClickListener {
            val intent = Intent(this, LoginCommon::class.java)
            startActivity(intent)
        }

        binding.textViewSignup.setClickablePart(
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
