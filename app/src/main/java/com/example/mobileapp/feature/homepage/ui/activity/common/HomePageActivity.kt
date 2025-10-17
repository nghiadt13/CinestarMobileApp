package com.example.mobileapp.feature.homepage.ui.activity.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp.databinding.ActivityHomepageBinding

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHomepageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}