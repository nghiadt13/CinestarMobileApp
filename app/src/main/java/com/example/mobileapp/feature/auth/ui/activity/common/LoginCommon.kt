package com.example.mobileapp.feature.auth.ui.activity.common

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.R
import com.example.mobileapp.databinding.ActivitySignInCommonBinding

class LoginCommon : AppCompatActivity() {
    private lateinit var binding: ActivitySignInCommonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInCommonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Xử lý back button - quay về trang trước
        binding.buttonViewBackButton.setOnClickListener { finish() }

    } 
}
